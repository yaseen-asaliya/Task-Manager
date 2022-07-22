package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.service.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    public final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class.getName());
    private Services userService;

    public UserRestController(){
    }
    @Autowired
    public UserRestController(@Qualifier("userServiceImplementation") Services userService) {
        this.userService = userService;
        LOGGER.info("User Controller created successfully");
    }

    @GetMapping("/users")
    public List<Object> getUsers(){
        return userService.findAll();
    }

    @GetMapping("/users/{userId}")
    public Object getUserById(@PathVariable int userId){
        return userService.findById(userId);
    }

    @PostMapping("/users")
    public String addUser(@RequestBody User user){
        if(user == null){
            throw new NullPointerException("User is null");
        }
        user.setId(0);
        userService.saveObject(user);
        LOGGER.debug("user added completed.");
        return user.toString() + " added successfully.";
    }

    @PutMapping("/users")
    public String updateUser(@RequestBody User user){
        if(user == null){
            throw new NullPointerException("Task is null");
        }
        userService.saveObject(user);
        LOGGER.debug("User updated completed.");
        return user.toString() + " updated successfully.";
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable int userId){
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