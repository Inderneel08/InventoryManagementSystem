package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.DAO.FinalOrder;

@Repository
public interface FinalOrderRepository extends JpaRepository<FinalOrder, String> {

}
