package com.example.demo.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ServiceLayer.FinalOrderServiceLayer;
import com.example.demo.ServiceLayer.OrderServiceLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class OrderController {

    @Autowired
    private FinalOrderServiceLayer finalOrderServiceLayer;

    @Autowired
    private OrderServiceLayer orderServiceLayer;

    @PostMapping("/getOrderInfo")
    public ResponseEntity<?> fetchOrderInfo(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("emailId");

        String orderId = (String) request.get("orderId");

        String dateofPurchase = (String) request.get("dateOfPurchase");

        System.out.println(email);

        System.out.println(orderId);

        System.out.println(dateofPurchase);

        if (email != null) {
            System.out.println("Type of email :->" + email.getClass());
        }

        if (orderId != null) {
            System.out.println("Type of orderId :->" + orderId.getClass());
        }

        if (dateofPurchase != null) {
            System.out.println("Type of date of purchase :->" + dateofPurchase.getClass());
        }

        return (ResponseEntity.ok().body("Succesful"));
    }

    @PostMapping("/fetchOrders")
    public ResponseEntity<?> fetchOrdersViaOrderId(@RequestBody Map<String, Object> request) {
        String orderId = (String) request.get("orderId");

        List<?> orders = orderServiceLayer.fetchOrders(orderId);

        return (ResponseEntity.ok().body(orders));
    }

}
