package com.example.demo.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.Orders;
import com.example.demo.Repository.OrderRepository;
import java.util.*;

@Service
public class OrderServiceLayer {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrders(Orders orders) {
        orderRepository.save(orders);
    }

    public List<?> fetchOrders(String orderId) {
        List<?> listOforders = orderRepository.fetchOrders(orderId);

        return (listOforders);
    }
}
