package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    // Create User
    UserDto createUser(UserDto userDto);

    // Update User
    UserDto updateUser(UserDto userWithUpdatedDetails, String userId);

    // Get All Users
    List<UserDto> getAllUsers();

    // Delete User
    void deleteUser(String userId);

    // Get single user by Id
    UserDto getUserById(String userId);

    // Get single user by email
    UserDto getUserByEmail(String userEmail);

    // Search User by name containing some characters
    UserDto searchUser(String keyword);

    // Other User Specific Service Methods
}
