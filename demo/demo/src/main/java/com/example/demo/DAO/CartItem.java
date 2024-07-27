package com.example.demo.DAO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItem {
    private int count;

    private String productId;

    private Double pricePerItem;

    private String path;

    private String productName;
}
