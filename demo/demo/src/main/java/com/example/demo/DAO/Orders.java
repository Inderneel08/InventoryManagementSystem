package com.example.demo.DAO;

import java.math.BigInteger;

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

    @Column(name = "product")
    private String product;

    @Column(name = "countAllproducts")
    private String countAllproducts;

    @Column(name = "totalAmount")
    private String totalAmount;

    @Column(name = "state")
    private int state;

    @Column(name = "address")
    private String address;

    @Column(name = "otp")
    private int otp;
}
