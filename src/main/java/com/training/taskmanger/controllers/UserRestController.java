package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.service.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserRestController {
    public final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class.getName());
    private final String UNAUTHORIZED_MESSAGE = "You're unauthorized";
    private Services userService;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private UserRepository userRepository;

    public UserRestController(){
    }
    @Autowired
    public UserRestController(@Qualifier("userServiceImplementation") Services userService) {
        this.userService = userService;
        LOGGER.info("User Controller created successfully");
    }

    // Get current user information
    @GetMapping("/user")
    public User getCurrentUser(){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        return (User) userService.findById(userId);
    }

    // Update current user information
    @PutMapping("/user")
    public String updateUser(@RequestBody User user){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(userId);
        userService.saveObject(user);
        LOGGER.debug("User updated completed.");
        return user + " updated successfully.";
    }

    // Delete Current user
    @DeleteMapping("/user")
    public String deleteUser(){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        User tempUser = (User)userService.findById(userId);
        userService.deleteById(userId);
        LOGGER.debug("User deleted completed.");
        return tempUser.toString() + " deleted successfully.";
    }
    private boolean isSignout() {
        int userId = authTokenFilter.getUserId();
        Optional<User> user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (user.get().getSignout()) {
            return true;
        }
        return false;
    }
    private void checkIfLogin(){
        if(isSignout()){
            throw new RuntimeException("You're unauthorized");
        }
    }

}