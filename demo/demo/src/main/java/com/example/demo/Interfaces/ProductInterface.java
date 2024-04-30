package com.example.demo.Interfaces;

import java.math.BigInteger;
import java.util.List;

import com.example.demo.DAO.Product;

public interface ProductInterface {
    public boolean createProduct(Product product);

    public List<Product> getAllProducts();

    public List<Product> backProducts(BigInteger id);

    public List<Product> fowardProducts(BigInteger id);
}
