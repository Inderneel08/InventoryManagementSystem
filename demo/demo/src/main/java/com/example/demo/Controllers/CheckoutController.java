package com.example.demo.Controllers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.CartItem;
import com.example.demo.DAO.OperationIdOTP;
import com.example.demo.DAO.Orders;
import com.example.demo.DAO.ResponseOtp;
import com.example.demo.ServiceLayer.CheckoutServiceLayer;
import com.example.demo.ServiceLayer.CustomUserDetailsServices;
import com.example.demo.ServiceLayer.FinalOrderServiceLayer;
import com.example.demo.ServiceLayer.OrderServiceLayer;
import com.example.demo.ServiceLayer.OtpServiceLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class CheckoutController {

    @Autowired
    private CheckoutServiceLayer checkoutServiceLayer;

    @Autowired
    private OtpServiceLayer otpServiceLayer;

    @Autowired
    private FinalOrderServiceLayer finalOrderServiceLayer;

    @Autowired
    private OrderServiceLayer orderServiceLayer;

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @PostMapping("/confirmOrder")
    public ResponseEntity<?> reviewOtpForOrdering(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");

        String state = (String) request.get("state");

        String billingAddress = (String) request.get("billingAddress");

        String shippingAddress = (String) request.get("shippingAddress");

        List<Object> cartItems = (List<Object>) request.get("cartItems");

        Double totalAmount = (Double) (request.get("totalAmount"));

        Double netAmount = (Double) (request.get("netAmount"));

        String pincode = (String) request.get("pincode");

        Integer otp = (Integer.parseInt((String) request.get("otp")));

        BigInteger operationId = (BigInteger) request.get("operationId");

        Integer operation = (Integer.parseInt((String) request.get("operation")));

        // Incorrect OTP.
        if (!otpServiceLayer.validateOtpForCheckout(operation, otp, operationId)) {
            return (ResponseEntity.status(666).build());
        }

        String orderId = finalOrderServiceLayer.createFinalOrder();

        finalOrderServiceLayer.createOrder(orderId, totalAmount, netAmount, pincode, operationId, email, state,
                billingAddress, shippingAddress);

        for (Object item : cartItems) {
            Orders orders = new Orders();

            CartItem cartItem = (CartItem) item;

            orders.setOrderId(orderId);

            orders.setProductId(cartItem.getProductId());

            orders.setCountProducts(cartItem.getCount());

            orders.setDate(LocalDateTime.now());

            try {
                orderServiceLayer.createOrders(orders);
            } catch (Exception e) {
                e.printStackTrace();

                return (ResponseEntity.badRequest().body(createResponse("Order could not be confirmed")));
            }
        }

        return (ResponseEntity.ok().body(createResponse("Order confirmed")));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, String> request) {

        String email = request.get("email");

        checkoutServiceLayer.sendOtpForOrder(email);

        OperationIdOTP getOTP = customUserDetailsServices.findOtpByEmail(email, 1);

        return (ResponseEntity.ok().body(createOtpResponse(
                "Please confirm the OTP send on your mail to proceed with the transaction.", getOTP.getOperationId(),
                1)));
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createOtpResponse(String message, BigInteger operationId, int operation) {
        Map<String, Object> response = new HashMap<>();

        ResponseOtp responseOtp = new ResponseOtp(message, operationId, operation);

        response.put("responseOtp", responseOtp);

        return response;
    }
}
