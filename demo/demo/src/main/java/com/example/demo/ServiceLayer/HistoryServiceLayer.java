package com.example.demo.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.DAO.FinalOrder;
import com.example.demo.Repository.FinalOrderRepository;

@Service
public class HistoryServiceLayer {

    @Autowired
    private FinalOrderRepository finalOrderRepository;

    public Page<?> findHistoryByEmail(String email, Pageable pageable) {
        Page<?> finalorders = finalOrderRepository.fetchOrdersByEmail(email, pageable);

        return (finalorders);
    }
}
