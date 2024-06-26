package com.example.demo.ServiceLayer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.demo.DAO.Otp;
import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailServiceLayer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpServiceLayer otpServiceLayer;

    public void registerEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        Random random = new Random();

        Otp otp = new Otp();

        int randomNumber = 1000000 + random.nextInt(900000);

        otp.setOtp(randomNumber);
        otp.setOperation(0);
        otp.setDate(Date.valueOf(LocalDate.now()));

        while (!otpServiceLayer.createOtp(otp)) {
            randomNumber = 1000000 + random.nextInt(900000);

            otp.setOtp(randomNumber);
        }

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text + String.valueOf(randomNumber) + ".");

        mailSender.send(message);
    }

}
