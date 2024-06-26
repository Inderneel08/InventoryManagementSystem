package com.example.demo.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Aspect.ExtractEmail;
import com.example.demo.Aspect.SendEmail;
import com.example.demo.ServiceLayer.RegistrationLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class RegisterController {

    @Autowired
    private RegistrationLayer registrationLayer;

    @PostMapping("/register")
    @SendEmail
    @ExtractEmail
    public ResponseEntity<?> register(@RequestBody Map<String, String> registrationRequest) {

        if (!registrationLayer.register(registrationRequest)) {
            return ResponseEntity.badRequest().body(createResponse("User already exists..."));
        }

        return ResponseEntity.ok(createResponse(
                "Registration successful an email containing the OTP has been sent to your email account to confirm you account."));
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

}
