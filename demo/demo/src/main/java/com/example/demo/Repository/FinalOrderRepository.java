package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.DAO.FinalOrder;
import jakarta.transaction.Transactional;

@Repository
public interface FinalOrderRepository extends JpaRepository<FinalOrder, String> {

    @Query(value = "SELECT * FROM finalorder WHERE finalorder.orderId = :orderId AND finalorder.date = CURRENT_DATE", nativeQuery = true)
    FinalOrder fetchOrderByOrderId(String orderId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE finalorder set finalorder.paid=1 where finalorder.orderId = :orderId", nativeQuery = true)
    void setStatusOfPaymentSuccess(String orderId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE finalorder set finalorder.paid=-1 where finalorder.orderId = :orderId", nativeQuery = true)
    void setStatusOfPaymentFailed(String orderId);
}
