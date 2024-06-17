package com.example.demo.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ServiceLayer.OneTimeServiceLayer;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class OneTimeController {

    @Autowired
    private OneTimeServiceLayer oneTimeServiceLayer;

    @GetMapping("/getAllStates")
    public ResponseEntity<?> getStates() {

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("states", oneTimeServiceLayer.getAllStates());

        return(ResponseEntity.ok().body(responseData));
    }

}
