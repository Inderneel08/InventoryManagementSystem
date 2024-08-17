package com.example.demo.Security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.Jwt.JwtTokenProvider;

import lombok.val;

@Component
public class JwtAutheticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get JWT header.
        // Bearer.
        // Validate the header.

        String requestTokenHeader = request.getHeader("Authorization");

        String role = request.getHeader("Role");

        String jwtToken = null;

        System.out.println(requestTokenHeader);

        System.out.println(role);

        // boolean value = requestTokenHeader != null &&
        // requestTokenHeader.startsWith("Bearer ")
        // && !requestTokenHeader.substring(7).equals("null");

        // System.out.println(value);

        // System.out.println(requestTokenHeader.substring(7).getClass());

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")
                && !requestTokenHeader.substring(7).equals("null")) {

            jwtToken = requestTokenHeader.substring(7);

            if (!jwtTokenProvider.isTokenExpired(jwtToken)) {
                String servletPath = request.getServletPath();

                if (jwtTokenProvider.validateToken(jwtToken)) {

                    if (servletPath.equals("/createProduct") || (servletPath.equals("/uploadImage"))) {
                        if (role.equals("ADMIN")) {
                            System.out.println("To filter");
                            filterChain.doFilter(request, response);
                        }
                    } else {
                        if (servletPath.equals("/checkout") || (servletPath.equals("/onlinePayment"))
                                || (servletPath.equals("/confirmOrder"))) {
                            if (role.equals("USER")) {
                                filterChain.doFilter(request, response);
                            } else {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            }
                        } else {
                            filterChain.doFilter(request, response);
                        }
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else {
            // System.out.println("Here.....");

            String servletPath = request.getServletPath();

            if ((servletPath.equals("/login")) || (servletPath.equals("/register"))
                    || (servletPath.equals("/adminLogin")) || (servletPath.equals("/getAllProducts"))
                    || ((servletPath.equals("/forward")))
                    || (servletPath.equals("/previous")) || (servletPath.equals("/getAllStates"))
                    || (servletPath.equals("/checkout"))
                    || (servletPath.equals("/confirmOtp")) || (servletPath.equals("/confirmOrder"))
                    || (servletPath.equals("/getOrderInfo")) || (servletPath.equals("/onlinePayment"))) {

                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

}
