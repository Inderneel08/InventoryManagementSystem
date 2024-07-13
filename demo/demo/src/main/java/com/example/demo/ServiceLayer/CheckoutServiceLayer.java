package com.example.demo.ServiceLayer;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.Orders;
import com.example.demo.DAO.Otp;
import com.example.demo.Repository.FinalOrderRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.OtpRepository;

@Service
public class CheckoutServiceLayer {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FinalOrderRepository finalOrderRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailServiceLayer emailServiceLayer;

    public void sendOtpForOrder(String email) {
        emailServiceLayer.registerEmail(email,"Confirm Order OTP","Please confirm OTP for proceeding with the order.", 1);
    }

    public void createOrders(Orders order) {

    }

}
