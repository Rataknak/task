package com.rataknak.userservice.service;

import com.rataknak.userservice.Entity.Role;
import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.dto.LoginRequest;
import com.rataknak.userservice.dto.RegisterRequest;

import java.util.List;

public interface UserService {
    String generateAndSendOtp(String email); // Returns a temporary token containing email and OTP
    String verifyOtp(String tempToken, String otp); // Returns a token indicating email is verified
    User registerUser(String verifiedEmailToken, RegisterRequest registerRequest, Role role);
    User loginUser(LoginRequest loginRequest);
    List<User> getAllUsers();
    User AdminRole(String token, RegisterRequest request);
    User UserRole(String token, RegisterRequest request);

}