package com.example.demo.ServiceLayer;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.Product;
import com.example.demo.Interfaces.ProductInterface;
import com.example.demo.Repository.ProductRepository;

@Service
public class ProductServiceLayer implements ProductInterface {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean createProduct(Product product) {

        if (productRepository.findByProductId(product.getProductId()) != null) {
            return (false);
        }

        productRepository.save(product);

        return (true);
    }

    public String productId(BigInteger id) {
        Product productId = productRepository.findStringByProductId(id);

        return(productId.getProductId());
    }

    @Override
    public List<Product> getAllProducts() {
        return (productRepository.findProductsUsingLimit());
    }

    @Override
    public List<Product> backProducts(BigInteger id) {
        return (productRepository.findBackProducts(id));
    }

    @Override
    public List<Product> fowardProducts(BigInteger id) {
        return (productRepository.findForwardProducts(id));
    }

}
