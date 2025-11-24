package com.rataknak.userservice.service;

import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.dto.LoginRequest;
import com.rataknak.userservice.dto.RegisterRequest;

import java.util.List;

public interface UserService {
    String generateAndSendOtp(String email); // Returns a temporary token containing email and OTP
    String verifyOtp(String tempToken, String otp); // Returns a token indicating email is verified
    User registerUser(String verifiedEmailToken, RegisterRequest registerRequest); // Registers user after email is verified
    User loginUser(LoginRequest loginRequest);
    List<User> getAllUsers();
}