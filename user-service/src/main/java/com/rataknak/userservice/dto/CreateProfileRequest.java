package com.rataknak.userservice.dto;

import lombok.Data;

@Data
public class CreateProfileRequest {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String profilePicture;
}
