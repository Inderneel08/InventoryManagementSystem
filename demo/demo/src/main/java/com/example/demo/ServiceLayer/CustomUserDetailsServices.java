package com.example.demo.ServiceLayer;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.OperationIdOTP;
import com.example.demo.DAO.Otp;
import com.example.demo.DAO.User;
import com.example.demo.Repository.OtpRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class CustomUserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (userRepository.findByEmail(email) != null) {
            return (UserDetails) userRepository.findByEmail(email);
        } else {
            return (null);
        }
    }

    public boolean checkVerifiedStatus(String email) {
        User user = userRepository.findByEmail(email);

        if (user.getIsVerified() == -1) {
            return (false);
        }

        return (true);
    }

    public boolean checkgoogleOAuthStatus(String email) {
        User user = userRepository.findByEmail(email);

        if (user.getIsOAuthGoogle() == -1) {
            return (false);
        }

        return (true);
    }

    public OperationIdOTP findOtpByEmail(String email, int operation) {
        User user = userRepository.findByEmail(email);

        Otp otp = null;

        if (user == null) {
            otp = otpRepository.findByOperationIdAndOperation(BigInteger.ZERO, operation);
        } else {
            otp = otpRepository.findByOperationIdAndOperation(user.getId(), operation);
        }

        OperationIdOTP operationIdOTP = new OperationIdOTP();

        if (otp == null) {
            operationIdOTP.setOtp(-1);
            operationIdOTP.setOperationId(user.getId());
            return (operationIdOTP);
        }

        operationIdOTP.setOtp(otp.getOtp());

        if (user == null) {
            operationIdOTP.setOperationId(BigInteger.ZERO);
        } else {
            operationIdOTP.setOperationId(user.getId());
        }

        return (operationIdOTP);
    }

    public void increaseOtpTries(BigInteger userId) {
        User user = userRepository.findByUserId(userId);

        if (user.getIncorrectOtpTries() < 3) {
            userRepository.increaseOtpTries(userId);
        }
    }

    public boolean checkMaxOtpTriesExceedThree(String email) {
        User user = userRepository.findByEmail(email);

        if (user.getIncorrectOtpTries() >= 3) {

            userRepository.resetOtpTries(email);

            return (true);
        }

        return (false);
    }

    public void createOAuth2GoogleUser(String email) {

        User user = new User();

        user.setEmail(email);

        user.setRole("OAUTH_USER_GOOGLE");

        user.setIsVerified(1);

        user.setIsOAuthGoogle(1);

        userRepository.save(user);
    }

}
