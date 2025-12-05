package com.rataknak.userservice.Controller;

import com.rataknak.userservice.Entity.Role;
import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.dto.EmailRequest;
import com.rataknak.userservice.dto.JwtResponse;
import com.rataknak.userservice.dto.LoginRequest;
import com.rataknak.userservice.dto.RegisterRequest;
import com.rataknak.userservice.dto.VerifyOtpRequest;
import com.rataknak.userservice.service.UserService;
import com.rataknak.userservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(
                    request.getVerifiedEmailToken(),
                    request,
                    Role.ADMIN
            );
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful!");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }  catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(
                    request.getVerifiedEmailToken(),
                    request,
                    Role.USER
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Registration successful!");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest);
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new JwtResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name()));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (authenticatedUser == null || !authenticatedUser.getRole().name().equals("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) {
        try {
            String jsonResponse = userService.verifyOtp(verifyOtpRequest.getTempToken(), verifyOtpRequest.getOtp());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody EmailRequest emailRequest) {
        try {
            String jsonResponse = userService.generateAndSendOtp(emailRequest.getEmail());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}