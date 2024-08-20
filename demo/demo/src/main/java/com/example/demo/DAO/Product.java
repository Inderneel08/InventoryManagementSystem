package com.example.demo.DAO;

import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "productId")
    private String productId;

    @Column(name = "productName")
    private String productName;

    @Column(name = "count")
    private String count;

    @Column(name = "category")
    private String category;

    @Column(name = "costPerUnit")
    private String costPerUnit;

    @Column(name = "brand")
    private String brand;

    @Column(name = "discount")
    private int discount;

    @Column(name = "subcategory")
    private String subcategory;

    @Column(name = "path", columnDefinition = "TEXT")
    private String path;
}
