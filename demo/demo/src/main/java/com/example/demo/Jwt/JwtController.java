package com.example.demo.Jwt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.OperationIdOTP;
import com.example.demo.DAO.ResponseOtp;
import com.example.demo.DAO.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.ServiceLayer.CustomUserDetailsServices;
import com.example.demo.ServiceLayer.EmailServiceLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class JwtController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @Autowired
    private EmailServiceLayer emailServiceLayer;

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> jWtrequest) {
        UserDetails userDetails = customUserDetailsServices.loadUserByUsername(jWtrequest.get("email"));

        if (userDetails == null) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        if (!customUserDetailsServices.checkVerifiedStatus(jWtrequest.get("email"))) {
            OperationIdOTP getOTP = customUserDetailsServices.findOtpByEmail(jWtrequest.get("email"));

            if (getOTP.getOtp() == -1) {
                emailServiceLayer.registerEmail(jWtrequest.get("email"), "Confirm OTP for registering into account.",
                        "Please confirm the email id using this OTP.");

                return (ResponseEntity.status(666).body(
                        createOtpResponse(
                                "Your account has not been confirmed yet.An email containing an otp has been send to your account. Please verify the otp.",
                                getOTP.getOperationId())));
            }

            emailServiceLayer.sendPlainMail(jWtrequest.get("email"), "Confirm OTP for registering into account.",
                    "Please confirm the email id using this OTP " + getOTP + ".");

            return (ResponseEntity.status(666).body(
                    createOtpResponse(
                            "Your account has not been confirmed yet.An email containing an otp has been send to your account. Please verify the otp.",
                            getOTP.getOperationId())));
        }

        String[] authorities = (String[]) userDetails.getAuthorities().stream().map(Object::toString)
                .toArray(String[]::new);

        String authority = authorities[0];

        if (!authority.equals("USER")) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        if (!BCrypt.checkpw(jWtrequest.get("password"), userDetails.getPassword())) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        String generatedToken = jwtTokenProvider.generateToken(userDetails);

        System.out.println("JWT token " + generatedToken);

        return ResponseEntity.ok(new JwtToken(jWtrequest.get("email"), generatedToken, authority));
    }

    @PostMapping("/confirmOtp")
    public ResponseEntity<?> confirmOtp(@RequestBody Map<String, String> request) {
        
    }

    @PostMapping("/adminLogin")
    public ResponseEntity<?> generateTokenForAdmin(@RequestBody Map<String, String> jWtrequest) {
        UserDetails userDetails = customUserDetailsServices.loadUserByUsername(jWtrequest.get("email"));

        if (userDetails == null) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        String[] authorities = (String[]) userDetails.getAuthorities().stream().map(Object::toString)
                .toArray(String[]::new);

        String authority = authorities[0];

        if (!authority.equals("ADMIN")) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        if (!BCrypt.checkpw(jWtrequest.get("password"), userDetails.getPassword())) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        String generatedToken = jwtTokenProvider.generateToken(userDetails);

        System.out.println("JWT token " + generatedToken);

        return ResponseEntity.ok(new JwtToken(jWtrequest.get("email"), generatedToken, authority));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateTokenIdentity() {
        return (ResponseEntity.ok("VALIDATED"));
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createOtpResponse(String message, BigInteger operationId) {
        Map<String, Object> response = new HashMap<>();

        ResponseOtp responseOtp = new ResponseOtp(message, operationId);

        response.put("responseOtp", responseOtp);

        return response;
    }

}
