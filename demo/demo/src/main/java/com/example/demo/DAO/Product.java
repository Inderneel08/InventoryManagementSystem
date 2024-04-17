package com.example.demo.DAO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "productName")
    private String productName;

    @Column(name = "count")
    private int count;

    @Column(name = "category")
    private String category;

    @Column(name = "costPerUnit")
    private double costPerUnit;

    @Column(name = "brand")
    private String brand;

    @Column(name = "discount")
    private int discount;

    @Column(name = "path", columnDefinition = "TEXT")
    private String path;
}
