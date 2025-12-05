package com.rataknak.userservice.Repository;

import com.rataknak.userservice.Entity.User;
import com.rataknak.userservice.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
}
