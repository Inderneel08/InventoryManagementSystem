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

        // || (servletPath.equals("/createProduct"))

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
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
                        filterChain.doFilter(request, response);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else {
            String servletPath = request.getServletPath();

            if ((servletPath.equals("/login")) || (servletPath.equals("/register"))
                    || (servletPath.equals("/adminLogin")) || (servletPath.equals("/getAllProducts"))|| ((servletPath
                            .equals("/forward")))||(servletPath.equals("/previous"))) {
                filterChain.doFilter(request, response);
            }
        }
    }

}
