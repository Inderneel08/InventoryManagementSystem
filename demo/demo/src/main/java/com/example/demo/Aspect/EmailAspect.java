package com.example.demo.Aspect;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.ServiceLayer.EmailServiceLayer;

@Aspect
@Component
public class EmailAspect {

    @Autowired
    private EmailServiceLayer emailServiceLayer;

    private String extractedEmail;

    @AfterReturning(pointcut = "@annotation(com.example.demo.Aspect.SendEmail)", returning = "result")
    public void sendEmailRegistration(Object result) {
        if (result instanceof Boolean && (Boolean) result) {
            if (extractedEmail != null) {
                String subject = "Registration Successful";
                String text = "Registration has been successfull use this OTP to confirm the email id ";

                emailServiceLayer.registerEmail(extractedEmail, subject, text);
            }
        }

        System.out.println("Extracted Email: -> " + extractedEmail);

        extractedEmail = null;
    }

    @Before("@annotation(com.example.demo.Aspect.ExtractEmail) && args(registrationRequest,..)")
    public void extractEmail(JoinPoint joinPoint, Map<String, String> registrationRequest) {
        extractedEmail = registrationRequest.get("email");
    }

}
