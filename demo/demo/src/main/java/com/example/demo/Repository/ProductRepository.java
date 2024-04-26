package com.example.demo.Repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.DAO.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, BigInteger> {

    @Query(value = "SELECT * FROM  products p where p.productId = :productId", nativeQuery = true)
    Product findByProductId(@Param("productId") String productId);

    @Query(value = "SELECT * FROM products LIMIT 6", nativeQuery = true)
    List<Product> findProductsUsingLimit();
}
