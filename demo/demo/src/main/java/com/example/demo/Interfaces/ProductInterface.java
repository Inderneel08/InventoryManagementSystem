package com.example.demo.Interfaces;

import java.util.List;

import com.example.demo.DAO.Product;

public interface ProductInterface {
    public boolean createProduct(Product product);

    public List<Product> getAllProducts();
}
