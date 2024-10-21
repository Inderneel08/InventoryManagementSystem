package com.example.demo.DAO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetails {

    private String productName;

    private String orderId;

    private String pricePerItem;

    private int countProducts;

    private Double totalAmount;

    private Double netAmount;

    private String email;
}
