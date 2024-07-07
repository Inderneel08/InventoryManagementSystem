package com.example.demo.ServiceLayer;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.DAO.Otp;
import com.example.demo.DAO.User;
import com.example.demo.Interfaces.OtpInterface;
import com.example.demo.Repository.OtpRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class OtpServiceLayer implements OtpInterface {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean createOtp(Otp otp) {
        Otp findOtp = otpRepository.findByOtp(otp.getOtp());

        if (findOtp != null) {
            System.out.println("Terminated");

            return (false);
        }

        otpRepository.save(otp);

        return (true);
    }

    public boolean verifyAccount(int operation, int otp, BigInteger operationId) {
        if (otpRepository.findByOperationIdOperationAndOtp(operation, operationId, otp) != null) {

            User user = userRepository.findByUserId(operationId);

            // userRepository.resetOtpTries(user.getEmail());

            userRepository.validateAccount(user.getEmail());

            return (true);
        }

        return (false);
    }

}
