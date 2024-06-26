package com.example.demo.ServiceLayer;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.DAO.User;
import com.example.demo.Interfaces.RegisterUserInterface;
import com.example.demo.Repository.UserRepository;

@Service
public class RegistrationLayer implements RegisterUserInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean register(Map<String, String> registrationRequest) {

        if (findUserByEmail(registrationRequest.get("email")) != null) {
            return (false);
        }

        User user = new User();

        user.setEmail(registrationRequest.get("email"));

        user.setPassword(passwordEncoder.encode(registrationRequest.get("password")));

        userRepository.save(user);

        return (true);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
