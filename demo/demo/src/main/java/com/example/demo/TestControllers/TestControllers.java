package com.example.demo.TestControllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.OrderDetails;
import com.example.demo.Repository.FinalOrderRepository;
import com.example.demo.ServiceLayer.FinalOrderServiceLayer;

@RestController
public class TestControllers {

    @Autowired
    private FinalOrderServiceLayer finalOrderServiceLayer;

    @Autowired
    private FinalOrderRepository finalOrderRepository;

    @GetMapping("/fetchOurList")
    public ResponseEntity<?> fetchList() {
        String email = "test123@yopmail.com";

        List<Object[]> finalOrderList = finalOrderRepository.fetchFinalOrderLatestViaEmail(email);

        return (ResponseEntity.ok().body(finalOrderList));

        // List<OrderDetails> orderDetailsList = new ArrayList<>();

        // for (Object[] row : finalOrderList) {
        //     OrderDetails orderDetails = new OrderDetails();

        //     orderDetails.setProductName((String) row[0]);
        //     orderDetails.setOrderId((String) row[1]);
        //     orderDetails.setCountProducts((Integer) row[2]);
        //     orderDetails.setTotalAmount(((BigDecimal) row[3]).doubleValue());
        //     orderDetails.setNetAmount(((BigDecimal) row[4]).doubleValue());

        //     orderDetailsList.add(orderDetails);
        // }

        // return (ResponseEntity.ok().body(orderDetailsList));
    }
}
