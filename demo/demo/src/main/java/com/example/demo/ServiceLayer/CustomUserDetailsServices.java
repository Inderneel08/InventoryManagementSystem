package com.example.demo.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.Repository.UserRepository;

@Service
public class CustomUserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (userRepository.findByEmail(email) != null) {
            return (UserDetails) userRepository.findByEmail(email);
        } else {
            return (null);
        }
    }

}
