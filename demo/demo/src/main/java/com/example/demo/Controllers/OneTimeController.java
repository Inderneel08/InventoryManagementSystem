package com.example.demo.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ServiceLayer.HistoryServiceLayer;
import com.example.demo.ServiceLayer.OneTimeServiceLayer;

import jakarta.websocket.server.PathParam;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class OneTimeController {

    @Autowired
    private OneTimeServiceLayer oneTimeServiceLayer;

    @Autowired
    private HistoryServiceLayer historyServiceLayer;

    @GetMapping("/getAllStates")
    public ResponseEntity<?> getStates() {

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("states", oneTimeServiceLayer.getAllStates());

        return (ResponseEntity.ok().body(responseData));
    }

    @GetMapping("/fetchOrderHistory")
    public ResponseEntity<?> fetchOrderHistory(@PathParam("email") String email,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<?> finalorders = historyServiceLayer.findHistoryByEmail(email, pageable);

        return (ResponseEntity.ok().body(finalorders));
    }

}
