package com.example.demo.ServiceLayer;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.DAO.FinalOrder;
import com.example.demo.Repository.FinalOrderRepository;

@Service
public class FinalOrderServiceLayer {

    @Autowired
    private FinalOrderRepository finalOrderRepository;

    public static String stringToSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String generatedOrderId() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        String orderId = sb.toString();

        String shaString = stringToSHA256(orderId + (LocalDate.now().toString()));

        return (shaString);
    }

    public String createFinalOrder() {
        String createOrderId = generatedOrderId();

        while (finalOrderRepository.fetchOrderByOrderId(createOrderId) != null) {
            createOrderId = generatedOrderId();
        }

        return (createOrderId);
    }

    public void createOrder(String orderId, Double totalAmount, Double netAmount, String pincode,
            BigInteger operationId, String email, String state, String billingAddress, String shippingAddress,
            String phoneNumber, int method) {
        FinalOrder finalOrder = new FinalOrder();

        finalOrder.setOrderId(orderId);

        finalOrder.setTotalAmount(totalAmount);

        finalOrder.setState(state);

        finalOrder.setBilling_address(billingAddress);

        finalOrder.setShipping_address(shippingAddress);

        finalOrder.setPincode(pincode);

        finalOrder.setDate(LocalDate.now());

        finalOrder.setOperationId(operationId);

        finalOrder.setNetAmount(netAmount);

        finalOrder.setEmail(email);

        finalOrder.setMethod(method);

        finalOrder.setPhoneNumber(phoneNumber);

        finalOrderRepository.save(finalOrder);
    }

    public void changeOrderStatus(String payment_status, String orderId) {
        if (payment_status.equals("SUCCESS")) {
            finalOrderRepository.setStatusOfPaymentSuccess(orderId);
        } else {
            finalOrderRepository.setStatusOfPaymentFailed(orderId);
        }
    }

}
