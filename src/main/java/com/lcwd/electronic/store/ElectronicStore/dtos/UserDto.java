package com.lcwd.electronic.store.ElectronicStore.dtos;

import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @NotBlank(message = "Specify whether user Active or Not !!")
    @Size(min = 6, max = 8, message = "Invalid Status !!")
    private String UserStatus;

    @Size(min = 5, max=25, message="Invalid Name !!")
    private String name;

//    @Email(message = "Invalid User Email !!")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}",
            message = "Invalid User email !!")
    @NotBlank(message = "Email is required !!")
    private String userEmail;

    @NotBlank(message = "Password is required !!")
    private String userPassword;

    @Size(max = 1, message = "Invalid gender !!")
    private String userGender;

    @NotBlank(message = "Write Something about yourself !!")
    private String userAbout;

    private List<RoleDto> roles;

    @ImageNameValid
    private String userProfileImage;
}
