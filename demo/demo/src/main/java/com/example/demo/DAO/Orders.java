package com.example.demo.DAO;

import java.math.BigInteger;
import java.time.Year;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "productId")
    private BigInteger productId;

    @Column(name = "countProducts")
    private int countProducts;

    @Column(name = "orderId")
    private String orderId;

    @Column(name = "year")
    private Year year;
}
