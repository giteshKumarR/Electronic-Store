package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import com.lcwd.electronic.store.ElectronicStore.payload.ImageResponse;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.services.FileService;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("v1/user-api")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    // create
    @PostMapping("/create-user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    // update
    @PutMapping("/update-user/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userWithUpdatedDetails,
                                              @PathVariable String userId) {
        return new ResponseEntity<>(userService.updateUser(userWithUpdatedDetails, userId), HttpStatus.OK);

    }

    // Update role of user to ADMIN
    @PutMapping("/update-role-to-admin/{userId}")
    public ResponseEntity<UserDto> updateRoletoAdmin(@PathVariable String userId) {
        return new ResponseEntity<>(userService.updateRoleToAdmin(userId), HttpStatus.OK);
    }


    //delete
    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {
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
    public ResponseEntity<PagableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "userName", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
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
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {
        return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.FOUND);
    }

    // Uploading user image
    @PostMapping("/upload-user-image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage")MultipartFile image,
            @PathVariable String userId
            ) throws IOException {


        // We need to put the image name in the user record in the database
        UserDto user = userService.getUserById(userId);
        String imagename = fileService.uploadImage(image, imageUploadPath);
        user.setUserProfileImage(imagename);
        UserDto userDto = userService.updateUser(user, userId);


        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imagename)
                .message("Image Uploaded Successfully..")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }


    // Serve User Image
    @GetMapping("/serve-user-image/{userId}")
    public void serverUserImage(
            @PathVariable String userId,
            HttpServletResponse response) throws IOException {
        UserDto user = userService.getUserById(userId);
        logger.info("user image name : {}", user.getUserProfileImage());
        InputStream resource = fileService.getResource(imageUploadPath, user.getUserProfileImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
