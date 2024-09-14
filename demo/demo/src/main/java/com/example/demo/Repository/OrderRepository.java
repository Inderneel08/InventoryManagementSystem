package com.example.demo.Repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.example.demo.DAO.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, BigInteger> {

    @Query(value = "SELECT * FROM orders where orders.orderId = :orderId", nativeQuery = true)
    public List<?> fetchOrders(String orderId);
}
