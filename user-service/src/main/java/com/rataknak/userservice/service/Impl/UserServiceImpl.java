package com.rataknak.userservice.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rataknak.userservice.Entity.Role;
import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.Entity.UserProfile;
import com.rataknak.userservice.Repository.UserRepository;
import com.rataknak.userservice.dto.LoginRequest;
import com.rataknak.userservice.dto.RegisterRequest;
import com.rataknak.userservice.service.UserService;

import com.rataknak.userservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;



    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtUtil jwtUtil;

    public UserServiceImpl() {
    }



    @Override
    @Transactional
    public User registerUser(String verifiedEmailToken, RegisterRequest registerRequest, Role role) {
        Claims claims = jwtUtil.extractClaims(verifiedEmailToken);

        String email = claims.getSubject();
        Boolean emailVerified = claims.get("emailVerified", Boolean.class);

        if (emailVerified == null || !emailVerified) {
            throw new RuntimeException("Email not verified. Please verify your email first.");
        }
        // Ensure the email from the token matches the email in the register request (if provided)
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Email mismatch between token and registration request.");
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(email);
        String hashedPassword = jwtUtil.hashPassword(registerRequest.getPassword());
        user.setPassword(hashedPassword);
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setRole(role);
        user.setEmailVerified(true);
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    @Override
    public User loginUser(LoginRequest loginRequest) {
        Optional<User> userOptional = Optional.empty();

        if (loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty()) {
            userOptional = userRepository.findByEmail(loginRequest.getEmail());
        } else if (loginRequest.getPhoneNumber() != null && !loginRequest.getPhoneNumber().isEmpty()) {
            userOptional = userRepository.findByPhoneNumber(loginRequest.getPhoneNumber());
        }

        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found!"));

        // Check if email is verified before allowing login
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email to login.");
        }

        if (!jwtUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("The password is incorrect!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public String verifyOtp(String tempToken, String otp) {
        Claims claims = jwtUtil.extractClaims(tempToken);

        String storedOtp = claims.get("otp", String.class);
        String email = claims.getSubject();

        if (!storedOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP.");
        }

        // If OTP is valid, create a new token indicating the email is verified
        String verifiedEmailToken = jwtUtil.generateVerifiedEmailToken(email);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "OTP verified successfully");
            responseMap.put("token", verifiedEmailToken);
            return mapper.writeValueAsString(responseMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error generating JSON response", e);
        }
    }



    @Override
    public String generateAndSendOtp(String email) {
        // Check if email is already registered
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered. Please login or reset password.");
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        // Send OTP email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Your OTP is: " + otp + ". It is valid for a limited time.");
        javaMailSender.send(message);

        // Create a temporary JWT token to hold email and OTP
        String token = jwtUtil.generateOtpToken(email, otp);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("success", true);
            responseMap.put("message", "OTP sent successfully");
            responseMap.put("token", token);
            return mapper.writeValueAsString(responseMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error generating JSON response", e);
        }
    }

    @Override
    @Transactional
    public User AdminRole(String token, RegisterRequest request) {
        return registerUser(token, request, Role.ADMIN);
    }

    @Override
    @Transactional
    public User UserRole(String token, RegisterRequest request) {
        return registerUser(token, request, Role.USER);
    }
}
