package com.example.demo.Repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.DAO.User;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

    User findByEmail(String email);
}
