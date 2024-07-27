package com.example.demo.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.Orders;
import com.example.demo.Repository.OrderRepository;

@Service
public class OrderServiceLayer {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrders(Orders orders) {
        orderRepository.save(orders);
    }

}
