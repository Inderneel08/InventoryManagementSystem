package com.example.demo.DAO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "finalorder")
public class FinalOrder {

    @Id
    @Column(name = "orderId")
    private String orderId;

    @Column(name = "totalAmount")
    private double totalAmount;

    @Column(name = "state")
    private String state;

    @Column(name = "address")
    private String address;

    @Column(name = "pincode")
    private int pincode;

    @Column(name = "otp")
    private int otp;
}
