package com.example.demo.ServiceLayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.OrderDetails;
import com.example.demo.DAO.Otp;
import com.example.demo.DAO.User;
import com.example.demo.Repository.UserRepository;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.mail.SimpleMailMessage;

@Service
public class EmailServiceLayer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpServiceLayer otpServiceLayer;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public void registerEmail(String to, String subject, String text, int operation) {
        SimpleMailMessage message = new SimpleMailMessage();

        Random random = new Random();

        User user = userRepository.findByEmail(to);

        Otp otp = new Otp();

        int randomNumber = 100000000 + random.nextInt(900000000);

        otp.setOtp(randomNumber);
        otp.setOperation(operation);
        otp.setDate(Date.valueOf(LocalDate.now()));

        if (user == null) {
            otp.setOperationId(BigInteger.ZERO);
        } else {
            otp.setOperationId(user.getId());
        }

        while (!otpServiceLayer.createOtp(otp)) {
            randomNumber = 1000000 + random.nextInt(900000);

            otp.setOtp(randomNumber);
        }

        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text + String.valueOf(randomNumber) + ".");
        mailSender.send(message);
    }

    public void sendPlainMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendEmailWithPdfInMemory(String toEmail, ByteArrayOutputStream pdfStream,
            List<OrderDetails> orderDetails) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Your Order Receipt - Order #" + orderDetails.get(0).getOrderId());
        helper.setText("Thank you for your order! Please find your receipt attached in PDF format.");

        InputStream inputStream = new ByteArrayInputStream(pdfStream.toByteArray());

        ByteArrayDataSource dataSource = new ByteArrayDataSource(inputStream, "application/pdf");

        helper.addAttachment("OrderReceipt.pdf", dataSource);

        mailSender.send(message);
    }

}
