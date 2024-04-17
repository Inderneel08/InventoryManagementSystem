package com.example.demo.Interfaces;

import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DAO.User;

public interface RegisterUserInterface {

    public boolean register(@RequestBody Map<String, String> registrationRequest);

    public User findUserByEmail(String email);
}
