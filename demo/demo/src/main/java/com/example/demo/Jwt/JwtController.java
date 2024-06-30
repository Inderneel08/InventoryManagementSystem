package com.example.demo.Jwt;

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
import com.example.demo.Aspect.ExtractEmail;
import com.example.demo.Repository.UserRepository;
import com.example.demo.ServiceLayer.CustomUserDetailsServices;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class JwtController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    @ExtractEmail
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> jWtrequest) {
        UserDetails userDetails = customUserDetailsServices.loadUserByUsername(jWtrequest.get("email"));

        if (userDetails == null) {
            return (ResponseEntity.badRequest()
                    .body(createResponse("Email id is not registered or the password is incorrect.")));
        }

        if(!customUserDetailsServices.checkVerifiedStatus(jWtrequest.get("email"))){
            return(ResponseEntity.status(666).body(createResponse("Your account has not been confirmed yet.An email containing an otp has been send to your account. Please verify the otp.")));
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

}
