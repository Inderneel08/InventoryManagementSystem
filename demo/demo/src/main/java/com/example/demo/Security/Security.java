package com.example.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.Jwt.JwtTokenProvider;
import com.example.demo.ServiceLayer.CustomUserDetailsServices;

@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomUserDetailsServices customUserDetailsServices;

    // @Override
    // protected void configure(HttpSecurity httpSecurity) throws Exception {
    // httpSecurity.csrf().disable().cors().and().authorizeRequests()
    // .anyRequest().permitAll()
    // .and()
    // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    // .and()
    // .formLogin().disable()
    // .logout().permitAll();
    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().cors().and().authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .formLogin().disable()
                .oauth2Login(oauth2 -> {
                    oauth2.defaultSuccessUrl("http://localhost:3000", true);
                    // oauth2.successHandler();
                })
                .logout(
                        logoutForm -> {
                            logoutForm.logoutUrl("/do-logout");
                            logoutForm.logoutSuccessUrl("http://localhost:3000");
                        });

        return (httpSecurity.build());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
