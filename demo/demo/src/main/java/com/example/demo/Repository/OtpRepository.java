package com.example.demo.Repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.DAO.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, BigInteger> {

    @Query(value = "SELECT * FROM otps o where o.otp = :otp AND o.date = CURRENT_DATE", nativeQuery = true)
    Otp findByOtp(@Param("otp") int opt);

    @Query(value = "SELECT * FROM otps o where o.operationId = :operationId AND o.operation = :operation AND o.date = CURRENT_DATE",nativeQuery = true)
    Otp findByOperationIdAndOperation(@Param("operationId") BigInteger operationId, @Param("operation") int operation);
}
