package com.example.demo.Controllers;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DAO.CartItem;
import com.example.demo.DAO.OperationIdOTP;
import com.example.demo.DAO.Orders;
import com.example.demo.DAO.ResponseOtp;
import com.example.demo.DAO.User;
import com.example.demo.ServiceLayer.CheckoutServiceLayer;
import com.example.demo.ServiceLayer.CustomUserDetailsServices;
import com.example.demo.ServiceLayer.FinalOrderServiceLayer;
import com.example.demo.ServiceLayer.OrderServiceLayer;
import com.example.demo.ServiceLayer.OtpServiceLayer;
import com.example.demo.ServiceLayer.ProductServiceLayer;
import com.cashfree.*;
import com.cashfree.model.CreateOrderRequest;
import com.cashfree.model.CustomerDetails;
import com.cashfree.model.OrderEntity;
import com.cashfree.model.OrderMeta;

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

    @Autowired
    private ProductServiceLayer productServiceLayer;

    // @Value("${cashfree.clientId}")
    // private String clientId;

    // @Value("${cashfree.clientSecret}")
    // private String clientSecret;

    @PostMapping("/confirmOrder")
    public ResponseEntity<?> reviewOtpForOrdering(
            @RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");

        String state = (String) request.get("state");

        String billingAddress = (String) request.get("billingAddress");

        String shippingAddress = (String) request.get("shippingAddress");

        ArrayList<LinkedHashMap<String, Object>> cartItems = (ArrayList<LinkedHashMap<String, Object>>) request
                .get("cartItems");

        Double totalAmount = (Double) (request.get("totalAmount"));

        Double netAmount = (Double) (request.get("netAmount"));

        String pincode = (String) request.get("pincode");

        Integer otp = (Integer.parseInt((String) request.get("otp")));

        BigInteger operationId = new BigInteger((String) request.get("operationId"));

        Integer operation = (Integer.parseInt((String) request.get("operation")));

        // Incorrect OTP.
        if (!otpServiceLayer.validateOtpForCheckout(operation, otp, operationId)) {
            return (ResponseEntity.status(666).build());
        }

        String orderId = finalOrderServiceLayer.createFinalOrder();

        finalOrderServiceLayer.createOrder(orderId, totalAmount, netAmount, pincode,
                operationId, email, state,
                billingAddress, shippingAddress);

        for (int i = 0; i < cartItems.size(); i++) {
            LinkedHashMap<String, Object> item = cartItems.get(i);

            CartItem cartItem = new CartItem();

            cartItem.setCount((Integer) item.get("count"));

            cartItem.setPath((String) item.get("path"));

            if (item.get("pricePerItem") instanceof Double) {
                cartItem.setPricePerItem((Double) item.get("pricePerItem"));
            } else if (item.get("pricePerItem") instanceof Integer) {
                cartItem.setPricePerItem(((Integer) item.get("pricePerItem")).doubleValue());
            }

            cartItem.setProductName((String) item.get("productName"));

            cartItem.setProductId(productServiceLayer.productId(BigInteger.valueOf((Integer) item.get("productId"))));

            Orders orders = new Orders();

            orders.setOrderId(orderId);

            orders.setProductId(cartItem.getProductId());

            orders.setCountProducts(cartItem.getCount());

            orders.setDate(LocalDateTime.now());

            try {
                orderServiceLayer.createOrders(orders);
            } catch (Exception e) {
                e.printStackTrace();

                return (ResponseEntity.badRequest().body(createResponse("Transaction failed.")));
            }
        }

        return (ResponseEntity.ok().body(createResponse(
                "Transaction successful." + "Please note the order id of your transaction " + orderId + ".")));
    }

    @PostMapping("/onlinePayment")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> paymentRequest) {

        System.out.println(paymentRequest);

        /* Payment Login */

        // Cashfree.XClientId = "";

        // Cashfree.XClientSecret = "";

        // Cashfree.XEnvironment = Cashfree.SANDBOX;

        String email = (String) paymentRequest.get("email");

        String phoneNumber = (String) paymentRequest.get("phoneNumber");

        Double netAmount = (Double) paymentRequest.get("netAmount");

        Double roundedAmount = Math.round(netAmount * 100.0) / 100.0;

        Double totalAmount = (Double) paymentRequest.get("totalAmount");

        UserDetails userDetails = customUserDetailsServices.loadUserByUsername(email);

        User user = null;

        if (userDetails != null) {
            user = (User) userDetails;
        }

        CustomerDetails customerDetails = new CustomerDetails();

        customerDetails.setCustomerId("0");

        if (user != null) {
            customerDetails.setCustomerId(user.getId().toString());
        }

        customerDetails.setCustomerEmail(email);

        customerDetails.setCustomerPhone(phoneNumber);

        CreateOrderRequest request = new CreateOrderRequest();

        System.out.println("Rounded Amount : ->" + roundedAmount);

        request.setOrderAmount(roundedAmount);

        request.setOrderCurrency("INR");

        request.setCustomerDetails(customerDetails);

        // Setting return url.

        OrderMeta orderMeta = new OrderMeta();

        orderMeta.setReturnUrl("http://localhost:3000/");

        request.setOrderMeta(orderMeta);

        try {
            Cashfree cashfree = new Cashfree();

            Cashfree.XClientId = "TEST10284318f45007527c473d39477181348201";

            Cashfree.XClientSecret = "cfsk_ma_test_0c6869d946184130107fc0cb23acbabc_4e020c8f";

            Cashfree.XEnvironment = Cashfree.SANDBOX;

            ApiResponse<OrderEntity> response = cashfree.PGCreateOrder("2023-08-01",
                    request, null, null, null);

            System.out.println(response);

            return (ResponseEntity.ok().body(response.getData()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
