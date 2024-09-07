package com.example.demo.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.DAO.FinalOrder;
import jakarta.transaction.Transactional;

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

    @Query(value = "SELECT finalorder.orderId,finalorder.billing_address,finalorder.totalAmount,finalorder.shipping_address,finalorder.pincode,finalorder.date,finalorder.netAmount,finalorder.email,finalorder.phoneNumber,finalorder.method,states.states from finalorder INNER JOIN states on finalorder.state=states.id where finalorder.email = :email", nativeQuery = true)
    Page<?> fetchOrdersByEmail(String email, Pageable pageable);
}
