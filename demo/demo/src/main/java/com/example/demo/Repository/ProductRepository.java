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

    @Query(value = "SELECT * FROM products LIMIT 12", nativeQuery = true)
    List<Product> findProductsUsingLimit();

    @Query(value = "SELECT * FROM products p where p.id < :id  order by  p.id desc LIMIT 12", nativeQuery = true)
    List<Product> findBackProducts(@Param("id") BigInteger id);

    @Query(value = "SELECT * FROM products p where p.id > :id  LIMIT 12", nativeQuery = true)
    List<Product> findForwardProducts(@Param("id") BigInteger id);

    @Query(value = "SELECT * FROM products p where p.id = :id", nativeQuery = true)
    Product findStringByProductId(@Param("id") BigInteger id);
}
