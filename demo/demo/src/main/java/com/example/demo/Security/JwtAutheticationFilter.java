package com.example.demo.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.Jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

@Component
public class JwtAutheticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // if (request.getServletPath().equals("login/oauth2/code/google")) {
        // System.out.println(request.getServletPath());
        // }

        System.out.println(request);

        String requestTokenHeader = request.getHeader("Authorization");

        String role = request.getHeader("Role");

        String jwtToken = null;

        System.out.println(requestTokenHeader);

        System.out.println(role);

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")
                && !requestTokenHeader.substring(7).equals("null")) {

            System.out.println("Hello1");

            jwtToken = requestTokenHeader.substring(7);

            if (!jwtTokenProvider.isTokenExpired(jwtToken)) {
                String servletPath = request.getServletPath();

                if (jwtTokenProvider.validateToken(jwtToken)) {

                    if (servletPath.equals("/createProduct") ||
                            (servletPath.equals("/uploadImage"))) {
                        if (role.equals("ADMIN")) {
                            System.out.println("To filter");
                            filterChain.doFilter(request, response);
                        } else {

                        }
                    } else {
                        if (servletPath.equals("/checkout") || (servletPath.equals("/onlinePayment"))
                                || (servletPath.equals("/confirmOrder")) || (servletPath.equals("/confirmation"))
                                || (servletPath.equals("/fetchOrderHistory"))) {
                            if (role.equals("USER") || role.equals("OAUTH_USER_GOOGLE")) {
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
            String servletPath = request.getServletPath();

            System.out.println("ServletPath : -> " + servletPath);

            System.out.println("Hello2");

            if ((servletPath.equals("/login")) || (servletPath.equals("/register"))
                    || (servletPath.equals("/adminLogin")) ||
                    (servletPath.equals("/getAllProducts"))
                    || ((servletPath.equals("/forward")))
                    || (servletPath.equals("/previous")) || (servletPath.equals("/getAllStates"))
                    || (servletPath.equals("/checkout"))
                    || (servletPath.equals("/confirmOtp")) ||
                    (servletPath.equals("/confirmOrder"))
                    || (servletPath.equals("/getOrderInfo")) ||
                    (servletPath.equals("/onlinePayment")) || (servletPath.equals("/confirmation"))) {

                System.out.println("Hello3");

                filterChain.doFilter(request, response);
            } else {

                System.out.println("Hello4");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request,
    // HttpServletResponse response,
    // FilterChain filterChain)
    // throws ServletException, IOException {

    // }

}
