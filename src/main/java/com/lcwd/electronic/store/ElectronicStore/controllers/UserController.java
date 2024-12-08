package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-api/")
public class UserController {
    @Autowired
    private UserService userService;

    // create
    @PostMapping("/create-user")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    // update
    @PutMapping("/update-user/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userWithUpdatedDetails,
                                              @PathVariable String userId) {
        return new ResponseEntity<>(userService.updateUser(userWithUpdatedDetails, userId), HttpStatus.OK);

    }
    //delete
    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        // Create a response message
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get All
    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // get single
    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.FOUND);
    }

    // get by email
    @GetMapping("/get-by-email/{userEmail}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String userEmail) {
        return new ResponseEntity<>(userService.getUserByEmail(userEmail), HttpStatus.FOUND);
    }

    // search user
    @GetMapping("/search-user/{keyword}")
    public ResponseEntity<UserDto> searchUser(@PathVariable String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.FOUND);
    }

}
