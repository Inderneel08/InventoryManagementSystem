package com.example.demo.Repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.DAO.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, BigInteger> {

    @Query(value = "Select * from products p where p.productName = :productName", nativeQuery = true)
    Product findByProductName(@Param("productName") String productName);

}
