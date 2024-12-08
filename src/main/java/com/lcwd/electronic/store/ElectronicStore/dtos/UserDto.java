package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private boolean isUserActive;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userGender;
    private String userAbout;
    private String userProfileImage;
}
