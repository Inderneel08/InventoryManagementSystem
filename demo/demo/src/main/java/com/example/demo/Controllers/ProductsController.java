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

import java.math.BigInteger;
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
        int back = 0;

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("back", back);
        responseData.put("products", productServiceLayer.getAllProducts());

        return (ResponseEntity.ok().body(responseData));
    }

    @PostMapping("/previous")
    public ResponseEntity<?> backProducts(@RequestBody Map<String, Object> jwtRequest) {
        BigInteger backId = BigInteger.valueOf(Long.parseLong((String) jwtRequest.get("id")));

        System.out.println(backId);

        Map<String, Object> responseData = new HashMap<>();

        List<Product> productList = productServiceLayer.backProducts(backId);

        responseData.put("next", 1);

        BigInteger lastINDEX = productList.get(productList.size() - 1).getId();

        List<Product> backProductList = productServiceLayer.backProducts(lastINDEX);

        if (backProductList.size() == 0) {
            responseData.put("back", 0);
        } else {
            responseData.put("back", 1);
        }

        Collections.reverse(productList);

        responseData.put("products", productList);

        return (ResponseEntity.ok().body(responseData));
    }

    @PostMapping("/forward")
    public ResponseEntity<?> forwardProducts(@RequestBody Map<String, Object> jwtRequest) {

        BigInteger forwardId = BigInteger.valueOf(Long.parseLong((String) jwtRequest.get("id")));

        System.out.println(forwardId);

        Map<String, Object> responseData = new HashMap<>();

        List<Product> productList = productServiceLayer.fowardProducts(forwardId);

        responseData.put("back", 1);
        responseData.put("products", productList);

        BigInteger lastINDEX = productList.get(productList.size() - 1).getId();

        List<Product> nextProductList = productServiceLayer.fowardProducts(lastINDEX);

        if (nextProductList.size() == 0) {
            responseData.put("next", 0);
        } else {
            responseData.put("next", 1);
        }

        return (ResponseEntity.ok().body(responseData));
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

        Path uploadDir = Paths.get(
                "C:/Users/inder/Documents/GitHub/InventoryManagementSystem/InventoryManagementSystemFrontend/inventorymanagementsystem/public/uploads/");

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

        response.put("imagePath",
                "C:/Users/inder/Documents/GitHub/InventoryManagementSystem/InventoryManagementSystemFrontend/inventorymanagementsystem/public/uploads/"
                        + filename);

        return (ResponseEntity.ok(response));
    }

    private Map<String, String> createResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }

}
