package com.example.demo.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class ProductsController {

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProducts(@RequestBody Map<String, String> jwtRequest) {
        return (ResponseEntity.ok("Product Created"));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("productImage") MultipartFile productImage) {
        String filename = UUID.randomUUID().toString() + "-" + productImage.getOriginalFilename();

        Path uploadDir = Paths.get("C:/Users/inder/Documents/GitHub/InventoryManagementSystem/demo/demo/src/main/resources/uploads");

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(filename);

            productImage.transferTo(filePath.toFile());
        } catch (Exception e) {
            e.printStackTrace();

            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image"));
        }

        Map<String, String> response = new HashMap<>();

        response.put("imagePath", "C:/Users/inder/Documents/GitHub/InventoryManagementSystem/demo/demo/src/main/resources/uploads" + filename);

        return (ResponseEntity.ok(response));
    }

}
