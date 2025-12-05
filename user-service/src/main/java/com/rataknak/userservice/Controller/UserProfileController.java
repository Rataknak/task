package com.rataknak.userservice.Controller;

import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.Entity.UserProfile;
import com.rataknak.userservice.dto.CreateProfileRequest;
import com.rataknak.userservice.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        UserProfile userProfile = userProfileService.getUserProfile(authenticatedUser);
        if (userProfile == null) {
            return new ResponseEntity<>("Profile not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("/me")
    public ResponseEntity<?> createMyProfile(@RequestBody CreateProfileRequest createProfileRequest, HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");
        if (authenticatedUser == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        UserProfile existingProfile = userProfileService.getUserProfile(authenticatedUser);
        if (existingProfile != null) {
            return new ResponseEntity<>("Profile already exists", HttpStatus.CONFLICT);
        }
        UserProfile newUserProfile = userProfileService.createUserProfile(authenticatedUser, createProfileRequest);
        return new ResponseEntity<>(newUserProfile, HttpStatus.CREATED);
    }
}
