package com.example.demo.Controllers;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.ServiceLayer.CheckoutServiceLayer;
import com.example.demo.ServiceLayer.OtpServiceLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class CheckoutController {

    @Autowired
    private CheckoutServiceLayer checkoutServiceLayer;

    @Autowired
    private OtpServiceLayer otpServiceLayer;

    @PostMapping("/confirmOrder")
    public ResponseEntity<?> reviewOtpForOrdering(@RequestBody Map<String, Object> request) {
        String state = (String) request.get("state");

        String billingAddress = (String) request.get("billingAddress");

        String shippingAddress = (String) request.get("shippingAddress");

        List<Object> cartItems = (List<Object>) request.get("cartItems");

        Double totalAmount = (Double) request.get("totalAmount");

        Double netAmount = (Double) request.get("netAmount");

        String pincode = (String) request.get("pincode");

        int otp = (int) request.get("otp");

        BigInteger operationId = (BigInteger) request.get("operationId");

        if (!otpServiceLayer.validateOtpForCheckout(1, otp, operationId)) {
            return (ResponseEntity.status(666).build());
        }

        

        for (Object item : cartItems) {

        }

        return (ResponseEntity.ok().body(createResponse("Order confirmed")));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        checkoutServiceLayer.sendOtpForOrder(email);

        return (ResponseEntity.ok().body("Please confirm the OTP send on your mail to proceed with the transaction."));
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

}
