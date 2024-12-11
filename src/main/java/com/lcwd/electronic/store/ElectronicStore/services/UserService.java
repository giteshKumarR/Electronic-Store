package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    // Create User
    UserDto createUser(UserDto userDto);

    // Update User
    UserDto updateUser(UserDto userWithUpdatedDetails, String userId);

    // Get All Users
    PagableResponse<UserDto> getAllUsers(Integer pageNumber,
                                      Integer pageSize,
                                      String sortBy,
                                      String sortDir);

    // Delete User
    void deleteUser(String userId) throws IOException;

    // Get single user by Id
    UserDto getUserById(String userId);

    // Get single user by email
    UserDto getUserByEmail(String userEmail);

    // Search User by name containing some characters
    List<UserDto> searchUser(String keyword);

    // Other User Specific Service Methods
}
