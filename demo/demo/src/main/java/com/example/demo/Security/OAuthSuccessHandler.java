package com.example.demo.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.ServiceLayer.CustomUserDetailsServices;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            OAuth2User user = oAuth2AuthenticationToken.getPrincipal();

            UserDetails userDetails = customUserDetailsServices
                    .loadUserByUsername(user.getAttributes().get("email").toString());

            if(userDetails==null){
                customUserDetailsServices.createOAuth2GoogleUser(user.getAttributes().get("email").toString());
            }

            if(userDetails.)
        }

        response.sendRedirect("http://localhost:3000/");
    }

}
