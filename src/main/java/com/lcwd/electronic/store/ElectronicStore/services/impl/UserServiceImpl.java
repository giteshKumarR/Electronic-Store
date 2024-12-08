package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        // We have to generated Unique Ids in string format
        String uniqueUserID = UUID.randomUUID().toString();
        userDto.setUserId(uniqueUserID);

        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);

        return entityToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userWithUpdatedDetails, String userId) {
        // Ub yaha request me DTO aayga to hum dto se entity banayenge to save the new details
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given userId"));
        user.setUserName(userWithUpdatedDetails.getUserName());
        // Email we are not going to set again
        if(!userWithUpdatedDetails.getUserEmail().equals(user.getUserEmail())) {
            throw new RuntimeException("Cannot change Email");
        }

        user.setUserPassword(userWithUpdatedDetails.getUserPassword());
        user.setUserGender(userWithUpdatedDetails.getUserGender());
        user.setUserActive(userWithUpdatedDetails.isUserActive());
        user.setUserAbout(userWithUpdatedDetails.getUserAbout());
        user.setUserProfileImage(userWithUpdatedDetails.getUserProfileImage());

        // Save the updated details
        User updatedUser = userRepository.save(user);
        return entityToDto(updatedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUserEntities = userRepository.findAll();
        List<UserDto> allUsersDtoList = allUserEntities.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return allUsersDtoList;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with given userId not found!!"));

        // Soft delete
//        user.setUserActive(false);
//        userRepository.save(user);

        //Hard Delete
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given userId"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with given email"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> allUserEntities = userRepository.findByUserNameLike(keyword);
        List<UserDto> allUsersDtoList = allUserEntities.stream().map(this::entityToDto).collect(Collectors.toList());
        return allUsersDtoList;
    }


    // DTO --> Entity
    private User dtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .isUserActive(userDto.isUserActive())
                .userName(userDto.getUserName())
                .userEmail(userDto.getUserEmail())
                .userPassword(userDto.getUserPassword())
                .userGender(userDto.getUserGender())
                .userAbout(userDto.getUserAbout())
                .userProfileImage(userDto.getUserProfileImage())
                .build();
        return user;
    }
    // Entity --> DTO
    private UserDto entityToDto(User savedUser) {
        UserDto userDto = UserDto.builder()
                .userId(savedUser.getUserId())
                .isUserActive(savedUser.isUserActive())
                .userName(savedUser.getUserName())
                .userEmail(savedUser.getUserEmail())
                .userPassword(savedUser.getUserPassword())
                .userGender(savedUser.getUserGender())
                .userAbout(savedUser.getUserAbout())
                .userProfileImage(savedUser.getUserProfileImage())
                .build();
        return userDto;
    }
}
