package com.example.demo.DAO;

import java.math.BigInteger;
import java.time.LocalDateTime;
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

    @Column(name = "billing_address")
    private String billing_address;

    @Column(name = "shipping_address")
    private String shipping_address;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "operationId")
    private BigInteger operationId;

    @Column(name = "netAmount")
    private double netAmount;
}
