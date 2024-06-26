package com.example.demo.ServiceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.DAO.Otp;
import com.example.demo.Interfaces.OtpInterface;
import com.example.demo.Repository.OtpRepository;

@Service
public class OtpServiceLayer implements OtpInterface {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public boolean createOtp(Otp otp) {
        Otp findOtp = otpRepository.findByOtp(otp.getOtp());

        if (findOtp == null) {
            return (false);
        }

        otpRepository.save(otp);

        return (true);
    }
}
