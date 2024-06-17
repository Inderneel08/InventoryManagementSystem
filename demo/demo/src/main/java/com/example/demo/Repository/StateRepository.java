package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.DAO.States;
import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<States, Integer> {

    @Query(value = "SELECT * FROM states", nativeQuery = true)
    List<States> findAllStates();
}
