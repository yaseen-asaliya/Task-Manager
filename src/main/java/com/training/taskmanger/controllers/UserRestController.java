package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.service.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    public final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class.getName());
    private Services userService;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    public UserRestController(){
    }
    @Autowired
    public UserRestController(@Qualifier("userServiceImplementation") Services userService) {
        this.userService = userService;
        LOGGER.info("User Controller created successfully");
    }


    @GetMapping("/user")
    public User getCurrentUser(){
        int userId = authTokenFilter.getId();
        return (User) userService.findById(userId);
    }

    @PutMapping("/user")
    public String updateUser(@RequestBody User user){
        int userId = authTokenFilter.getId();
        if(user == null){
            throw new NullPointerException("User is null");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(userId);
        userService.saveObject(user);
        LOGGER.debug("User updated completed.");
        return user + " updated successfully.";
    }

    @DeleteMapping("/user")
    public String deleteUser(){
        int userId = authTokenFilter.getId();
        User tempUser = (User)userService.findById(userId);
        if(tempUser==null){
            LOGGER.warn("Wrong user id passed");
            throw new NotFoundException("User with id -" + userId + "- not found.");
        }
        userService.deleteById(userId);
        LOGGER.debug("User deleted completed.");
        return tempUser.toString() + " deleted successfully.";
    }


}