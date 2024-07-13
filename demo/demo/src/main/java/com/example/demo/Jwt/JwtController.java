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
import com.example.demo.ServiceLayer.CustomUserDetailsServices;
import com.example.demo.ServiceLayer.EmailServiceLayer;
import com.example.demo.ServiceLayer.OtpServiceLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class JwtController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @Autowired
    private EmailServiceLayer emailServiceLayer;

    @Autowired
    private OtpServiceLayer otpServiceLayer;

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> jWtrequest) {
        UserDetails userDetails = customUserDetailsServices.loadUserByUsername(jWtrequest.get("email"));

        if (userDetails == null) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
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

        if (!customUserDetailsServices.checkVerifiedStatus(jWtrequest.get("email"))) {
            OperationIdOTP getOTP = customUserDetailsServices.findOtpByEmail(jWtrequest.get("email"));

            if (customUserDetailsServices.checkMaxOtpTriesExceedThree(jWtrequest.get("email"))) {
                emailServiceLayer.registerEmail(jWtrequest.get("email"), "Confirm OTP for registering into account.",
                        "Please confirm the email id using this OTP.", 0);

                getOTP = customUserDetailsServices.findOtpByEmail(jWtrequest.get("email"));

                return (ResponseEntity.status(666).body(
                        createOtpResponse(
                                "Your account has not been confirmed yet.An email containing an otp has been send to your account. Please verify the otp.",
                                getOTP.getOperationId(), 0)));
            }

            if (getOTP.getOtp() == -1) {
                emailServiceLayer.registerEmail(jWtrequest.get("email"), "Confirm OTP for registering into account.",
                        "Please confirm the email id using this OTP.", 0);

                return (ResponseEntity.status(666).body(
                        createOtpResponse(
                                "Your account has not been confirmed yet.An email containing an otp has been send to your account. Please verify the otp.",
                                getOTP.getOperationId(), 0)));
            }

            emailServiceLayer.sendPlainMail(jWtrequest.get("email"), "Confirm OTP for registering into account.",
                    "Please confirm the email id using this OTP " + getOTP.getOtp() + ".");

            return (ResponseEntity.status(666).body(
                    createOtpResponse(
                            "Your account has not been confirmed yet.An email containing an otp has been send to your account. Please verify the otp.",
                            getOTP.getOperationId(), 0)));
        }

        String generatedToken = jwtTokenProvider.generateToken(userDetails);

        System.out.println("JWT token " + generatedToken);

        return ResponseEntity.ok(new JwtToken(jWtrequest.get("email"), generatedToken, authority));
    }

    @PostMapping("/confirmOtp")
    public ResponseEntity<?> confirmOtp(@RequestBody Map<String, String> request) {
        BigInteger operationId = BigInteger.valueOf(Long.parseLong(request.get("operationId")));

        int operation = Integer.parseInt(request.get("operation"));

        int otp = Integer.parseInt(request.get("otp"));

        // User user = userRepository.findByUserId(operationId);

        System.out.println(operationId);

        System.out.println(operation);

        System.out.println(otp);

        if (!otpServiceLayer.verifyAccount(operation, otp, operationId)) {
            customUserDetailsServices.increaseOtpTries(operationId);

            return (ResponseEntity.status(305).body(createResponse("Incorrect OTP")));
        }

        return (ResponseEntity.ok().body(createResponse("Account has been verified")));
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

    private Map<String, Object> createOtpResponse(String message, BigInteger operationId, int operation) {
        Map<String, Object> response = new HashMap<>();

        ResponseOtp responseOtp = new ResponseOtp(message, operationId, operation);

        response.put("responseOtp", responseOtp);

        return response;
    }

}
