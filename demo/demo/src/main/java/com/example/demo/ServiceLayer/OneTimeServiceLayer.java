package com.example.demo.ServiceLayer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.DAO.States;
import com.example.demo.Interfaces.StateInterface;
import com.example.demo.Repository.StateRepository;

@Component
public class OneTimeServiceLayer implements StateInterface {

    @Autowired
    private StateRepository stateRepository;

    @Override
    public List<States> getAllStates() {
        return (stateRepository.findAllStates());
    }

}
