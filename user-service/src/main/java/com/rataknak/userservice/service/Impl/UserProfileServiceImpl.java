package com.rataknak.userservice.service.Impl;

import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.Entity.UserProfile;
import com.rataknak.userservice.Repository.UserProfileRepository;
import com.rataknak.userservice.dto.RegisterRequest;
import com.rataknak.userservice.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Override
    public UserProfile getUserProfile(User user) {
        return userProfileRepository.findByUser(user).orElse(null);
    }

    @Override
    @Transactional
    public UserProfile createUserProfile(User user, com.rataknak.userservice.dto.CreateProfileRequest createProfileRequest) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setFirstName(createProfileRequest.getFirstName());
        userProfile.setLastName(createProfileRequest.getLastName());
        if (createProfileRequest.getDateOfBirth() != null && !createProfileRequest.getDateOfBirth().isEmpty()) {
            userProfile.setDateOfBirth(LocalDate.parse(createProfileRequest.getDateOfBirth()));
        }
        userProfile.setGender(createProfileRequest.getGender());
        userProfile.setProfilePicture(createProfileRequest.getProfilePicture());
        userProfile.setCreatedAt(LocalDateTime.now());
        return userProfileRepository.save(userProfile);
    }
}
