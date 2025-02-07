package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Role;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.general.BadApiRequestException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.general.CannotChangeEmailException;
import com.lcwd.electronic.store.ElectronicStore.exceptions.general.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.helper.AppConstants;
import com.lcwd.electronic.store.ElectronicStore.helper.Helper;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import com.lcwd.electronic.store.ElectronicStore.repositories.RoleRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Override
    public UserDto createUser(UserDto userDto) {
        // We have to generated Unique Ids in string format
        String uniqueUserID = UUID.randomUUID().toString();
        userDto.setUserId(uniqueUserID);

        User user = dtoToEntity(userDto);
        // Encode the password before saving
        user.setUserPassword(passwordEncoder.encode(user.getPassword()));

        // Assign role to the user
        // Get the normal role form roleRepository
        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_"+ AppConstants.ROLE_NORMAL);

        // Means ki agar DB mei Normal role nahi mila to hum jo humne upar create kara hai
        // vo role assign kar denge...
        Role roleNormal = roleRepository.findByName("ROLE_"+AppConstants.ROLE_NORMAL).orElse(role);
        user.setRoles(List.of(roleNormal));
        User savedUser = userRepository.save(user);

        return entityToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userWithUpdatedDetails, String userId) {
        // Ub yaha request me DTO aayga to hum dto se entity banayenge to save the new details
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User not found with given userId"));
        user.setName(userWithUpdatedDetails.getName());
        // Email we are not going to set again
        if(!userWithUpdatedDetails.getUserEmail().equals(user.getUserEmail())) {
            throw new CannotChangeEmailException("Cannot Update Email");
        }

        user.setUserPassword(passwordEncoder.encode(userWithUpdatedDetails.getUserPassword()));
        // TODO: 1/2/2025
//        user.setRoles(userWithUpdatedDetails.getRoles().stream().map(roleDto -> mapper.map(roleDto, Role.class)).collect(Collectors.toList()));
        user.setUserGender(userWithUpdatedDetails.getUserGender());
        user.setUserStatus(userWithUpdatedDetails.getUserStatus());
        user.setUserAbout(userWithUpdatedDetails.getUserAbout());
        user.setUserProfileImage(userWithUpdatedDetails.getUserProfileImage());

        // Save the updated details
        User updatedUser = userRepository.save(user);
        return entityToDto(updatedUser);
    }

     @Override
    public PagableResponse<UserDto> getAllUsers(Integer pageNumber,
                                                     Integer pageSize,
                                                     String sortBy,
                                                     String sortDir) {

        // PageNumber default starts from zero
        // 1. We pass the pageNumber and pageSize to get a Pagable object
        //    Here the PageRequest.of() method will accept the pageNumber and pageSize.

//         Sort sort = Sort.by(sortBy);  // when we are sorting by only a field name

         // For Conditional sorting
         Sort sort = (sortDir.equalsIgnoreCase("desc"))
                 ? (Sort.by(sortBy).descending())
                 :(Sort.by(sortBy).ascending());

         // By default pageNumber starts with 0 but if we want to start with 1 just pass paguNumber-1 to below
         // function and in the request param pass 1 so here due to the below logic it will behave like the
         // page number is starting form 1 but at the backend it is 0 only..
         // Also one thing we need to just do a plus 1 for the current page number ie. pageObject.getPageNumber
         // in the Helper method in the Helper package as we changed the logic and getPageNumber (current page number)
         // Also represent the index of the page so it must be incremented....
         //
//         Pageable pagable = PageRequest.of(pageNumber-1, pageSize, sort);
         Pageable pagable = PageRequest.of(pageNumber, pageSize, sort);

        // 2. Pass the Pagable object to the .findAll method
        Page<User> pageObject = userRepository.findAll(pagable);

        // 3. We will not get the list directly for th findAll method, we will get a pagable object
        //    like shown above and then from that object if we do .getContent() then we will get the list..


         // This method is now a general method i.e it can be used for any of the
         // module to convert the pagable object to the response and return it...
         PagableResponse<UserDto> pagableResponse = Helper.getPagableResponse(pageObject, UserDto.class);

         return pagableResponse;
    }

    @Override
    public void deleteUser(String userId) {
        User userToBeDeleted = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User with given userId not found!!"));

        // Delete user image from the images folder
        // 1. We get the full path to delete the image
        String fullPath = imagePath + userToBeDeleted.getUserProfileImage();
        // 2. We need to create a path then pass that path to Delete function in Files to delete the file.

        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("User image not found in Folder");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Soft delete
//        user.setUserStatus("Inactive");
//        userRepository.save(user);

        //Hard Delete
        userToBeDeleted.getRoles().clear();
        User savedUserWithNoroles = userRepository.save(userToBeDeleted);// saving the user with no role

        userRepository.delete(savedUserWithNoroles);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("User not found with given userId"));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> new ResourseNotFoundException("User not found with given email"));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> allUserEntities = userRepository.findByNameContaining(keyword);
        List<UserDto> allUsersDtoList = allUserEntities.stream().map(this::entityToDto).collect(Collectors.toList());
        return allUsersDtoList;
    }

    @Override
    public UserDto updateRoleToAdmin(String userId) {
        User userToBeUpdated = userRepository.findById(userId).orElseThrow(() -> new ResourseNotFoundException("user with given Id not found !!"));
        String userRoleName = userToBeUpdated.getRoles().get(0).getName();
        // Means to change role user's role should be Normal and the user should not be inactive
        if(!userToBeUpdated.getUserStatus().equalsIgnoreCase("inactive") && userRoleName.equals("ROLE_NORMAL")) {
            // Make the roles list empty and then assign the new ADMIN role
            Role roleAdmin = roleRepository.findByName("ROLE_"+AppConstants.ROLE_ADMIN).orElseThrow(() -> new ResourseNotFoundException("Given role not found!!"));
            userToBeUpdated.getRoles().clear();
            userToBeUpdated.getRoles().add(roleAdmin);
        }
        // Save the updated user
        User savedUser = userRepository.save(userToBeUpdated);
        return mapper.map(savedUser, UserDto.class);
    }


    // DTO --> Entity
    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .isUserActive(userDto.isUserActive())
//                .userName(userDto.getUserName())
//                .userEmail(userDto.getUserEmail())
//                .userPassword(userDto.getUserPassword())
//                .userGender(userDto.getUserGender())
//                .userAbout(userDto.getUserAbout())
//                .userProfileImage(userDto.getUserProfileImage())
//                .build();
        return mapper.map(userDto, User.class);
    }
    // Entity --> DTO
    private UserDto entityToDto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .isUserActive(savedUser.isUserActive())
//                .userName(savedUser.getUserName())
//                .userEmail(savedUser.getUserEmail())
//                .userPassword(savedUser.getUserPassword())
//                .userGender(savedUser.getUserGender())
//                .userAbout(savedUser.getUserAbout())
//                .userProfileImage(savedUser.getUserProfileImage())
//                .build();
        return mapper.map(savedUser, UserDto.class);
    }
}
