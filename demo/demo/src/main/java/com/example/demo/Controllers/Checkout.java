package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.Orders;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class Checkout {

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> request) {

        String email = (String) request.get("email");

        String state = (String) request.get("state");

        String billingAddress = (String) request.get("billingAddress");

        String shippingAddress = (String) request.get("shippingAddress");

        List<Object> cartItems = (List<Object>) request.get("cartItems");

        Double totalAmount = (Double) request.get("netAmount");

        String pincode = (String) request.get("pincode");

        System.out.println(request);

        return (ResponseEntity.ok().body("Hello"));
    }

}
