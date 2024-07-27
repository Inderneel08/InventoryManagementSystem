package com.example.demo.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceLayer {

    @Autowired
    private EmailServiceLayer emailServiceLayer;

    public void sendOtpForOrder(String email) {

        emailServiceLayer.registerEmail(email, "Confirm Order OTP", "Please confirm OTP for proceeding with the order.",
                1);
    }

}
