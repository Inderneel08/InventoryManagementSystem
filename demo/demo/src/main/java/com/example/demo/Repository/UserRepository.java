package com.example.demo.Repository;

import java.math.BigInteger;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.DAO.User;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

    User findByEmail(String email);

    @Query(value = "SELECT * FROM users where users.id = :id", nativeQuery = true)
    User findByUserId(@Param("id") BigInteger id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users set users.incorrectOtpTries=users.incorrectOtpTries+1 where users.id = :userId", nativeQuery = true)
    void increaseOtpTries(@Param("userId") BigInteger userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users set users.incorrectOtpTries=0 where users.email = :email", nativeQuery = true)
    void resetOtpTries(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users set users.isVerified=1 where users.email = :email", nativeQuery = true)
    void validateAccount(@Param("email") String email);
}
