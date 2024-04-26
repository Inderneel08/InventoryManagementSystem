package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DAO.Product;
import com.example.demo.ServiceLayer.ProductServiceLayer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class ProductsController {

    @Autowired
    private ProductServiceLayer productServiceLayer;

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        return(ResponseEntity.ok().body(productServiceLayer.getAllProducts()));
    }

    @PostMapping("/createProduct")
    public ResponseEntity<?> createProducts(@RequestBody Map<String, String> jwtRequest) {
        Product product = new Product();

        product.setPath(jwtRequest.get("uploadedImage"));
        product.setBrand(jwtRequest.get("productBrand"));
        product.setSubcategory(jwtRequest.get("subCategory"));
        product.setCategory(jwtRequest.get("productCategory"));
        product.setCount(jwtRequest.get("productQuantity"));
        product.setProductName(jwtRequest.get("productName"));
        product.setCostPerUnit(jwtRequest.get("costPerUnit"));
        product.setProductId(jwtRequest.get("productId"));

        try {
            if (!productServiceLayer.createProduct(product)) {
                return (ResponseEntity.badRequest().body(
                        createResponse(
                                "Product Creation failed as a product already exists with the same product id")));
            }

        } catch (Exception e) {
            e.printStackTrace();

            return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage())));
        }

        return (ResponseEntity.ok(createResponse("Product created successfully")));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("productImage") MultipartFile productImage) {
        String filename = UUID.randomUUID().toString() + "-" + productImage.getOriginalFilename();

        Path uploadDir = Paths.get("C:/Users/inder/Desktop/uploads/");

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

        response.put("imagePath", "C:/Users/inder/Desktop/uploads/" + filename);

        return (ResponseEntity.ok(response));
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

}
