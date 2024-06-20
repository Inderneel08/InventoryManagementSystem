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

    @Column(name = "countProducts")
    private int countProducts;

    @Column(name = "costPerPiece")
    private double costPerPiece;

    @Column(name = "orderId")
    private String orderId;
}
