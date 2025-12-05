package com.rataknak.userservice.service;

import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.Entity.UserProfile;
import com.rataknak.userservice.dto.RegisterRequest;

import com.rataknak.userservice.dto.CreateProfileRequest;

public interface UserProfileService {
    UserProfile getUserProfile(User user);

    UserProfile createUserProfile(User user, CreateProfileRequest createProfileRequest);
}
