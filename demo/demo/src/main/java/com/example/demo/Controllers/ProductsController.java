package com.example.demo.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
public class ProductsController {

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProducts(@RequestBody Map<String, String> jwtRequest) {
        return (ResponseEntity.ok("Product Created"));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("productImage") MultipartFile productImage) {
        String filename = UUID.randomUUID().toString() + "-" + productImage.getOriginalFilename();

        String imagePath = "uploads/" + filename;

        try {
            productImage.transferTo(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();

            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image"));
        }

        Map<String,String> response = new HashMap<>();

        response.put("imagePath", imagePath);

        return (ResponseEntity.ok(response));
    }

}
