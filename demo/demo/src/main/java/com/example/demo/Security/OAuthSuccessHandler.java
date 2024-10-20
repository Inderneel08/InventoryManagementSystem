package com.example.demo.Security;

import java.io.IOException;

import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.Jwt.JwtToken;
import com.example.demo.Jwt.JwtTokenProvider;
import com.example.demo.ServiceLayer.CustomUserDetailsServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            OAuth2User user = oAuth2AuthenticationToken.getPrincipal();

            // System.out.println(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());

            UserDetails userDetails = customUserDetailsServices
                    .loadUserByUsername(user.getAttributes().get("email").toString());

            if (userDetails == null) {
                if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
                    customUserDetailsServices.createOAuth2GoogleUser(user.getAttributes().get("email").toString());
                }

                if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("facebook")) {
                    customUserDetailsServices.createOAuth2FacebookUser(user.getAttributes().get("email").toString());
                }
            }

            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("google")) {
                if (!customUserDetailsServices.checkgoogleOAuthStatus(user.getAttributes().get("email").toString())) {
                    // Already account exists via simple login.

                    Cookie errorCookie = new Cookie("errorMessage", "ACCOUNTEXISTS");
                    errorCookie.setPath("/");
                    errorCookie.setHttpOnly(false);
                    errorCookie.setMaxAge(60 * 5);
                    response.addCookie(errorCookie);

                    response.sendRedirect("http://localhost:3000/");

                    return;
                }
            }

            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("facebook")) {
                if (!customUserDetailsServices.checkgoogleOAuthStatus(user.getAttributes().get("email").toString())) {
                    // Already account exists via simple login.

                    Cookie errorCookie = new Cookie("errorMessage", "ACCOUNTEXISTS");
                    errorCookie.setPath("/");
                    errorCookie.setHttpOnly(false);
                    errorCookie.setMaxAge(60 * 5);
                    response.addCookie(errorCookie);

                    response.sendRedirect("http://localhost:3000/");

                    return;
                }

            }

            ObjectMapper objectMapper = new ObjectMapper();

            String generatedToken = jwtTokenProvider.generateToken(userDetails);

            String email = user.getAttributes().get("email").toString();

            String authority = "OAUTH_USER_GOOGLE";

            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("facebook")) {
                authority = "OAUTH_USER_FACEBOOK";
            }

            String jwtTokenJson = objectMapper.writeValueAsString(new JwtToken(email, generatedToken, authority));

            String encodedToken = Base64.getEncoder().encodeToString(jwtTokenJson.getBytes());

            Cookie tokenCookie = new Cookie("token", encodedToken);

            tokenCookie.setHttpOnly(false);

            tokenCookie.setMaxAge(60 * 60);

            tokenCookie.setPath("/");

            response.addCookie(tokenCookie);
        }

        response.sendRedirect("http://localhost:3000/");
    }

}
