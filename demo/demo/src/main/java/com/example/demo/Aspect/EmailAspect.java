package com.example.demo.Aspect;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.DAO.FinalOrder;
import com.example.demo.DAO.OrderDetails;
import com.example.demo.PdfGeneratorService.PdfGeneratorService;
import com.example.demo.ServiceLayer.EmailServiceLayer;
import com.example.demo.ServiceLayer.FinalOrderServiceLayer;
import com.example.demo.ServiceLayer.OrderServiceLayer;

@Aspect
@Component
public class EmailAspect {

    @Autowired
    private EmailServiceLayer emailServiceLayer;

    @Autowired
    private FinalOrderServiceLayer finalOrderServiceLayer;

    @Autowired
    private OrderServiceLayer orderServiceLayer;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    private String extractedEmail;

    @AfterReturning(pointcut = "@annotation(com.example.demo.Aspect.SendEmail)", returning = "result")
    public void sendEmailRegistration(Object result) {
        if (result instanceof ResponseEntity) {
            ResponseEntity<?> response = (ResponseEntity<?>) result;

            if (response.getStatusCodeValue() == 200 && extractedEmail != null) {
                System.out.println(1);
                String subject = "Registration Successful";
                String text = "Registration has been successfull use this OTP to confirm the email id ";

                emailServiceLayer.registerEmail(extractedEmail, subject, text, 0);
            }
        }

        System.out.println("Extracted Email: -> " + extractedEmail);

        extractedEmail = null;
    }

    @Before("@annotation(com.example.demo.Aspect.ExtractEmail) && args(registrationRequest,..)")
    public void extractEmail(JoinPoint joinPoint, Map<String, String> registrationRequest) {
        extractedEmail = registrationRequest.get("email");
    }

    @After("@annotation(com.example.demo.Aspect.SendReciept)")
    public void sendReciept() {
        List<OrderDetails> finalOrderList = finalOrderServiceLayer.fetchLastFinalOrderDetails(extractedEmail);

        try {
            ByteArrayOutputStream pdfStream = pdfGeneratorService.generateOrderInvoice(finalOrderList);

            emailServiceLayer.sendEmailWithPdfInMemory(extractedEmail, pdfStream, finalOrderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
