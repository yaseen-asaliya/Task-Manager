package com.training.TaskManger.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.training.TaskManger.Entity.User;
import com.training.TaskManger.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private Services userService;

    public UserRestController(){

    }
    @Autowired
    public UserRestController(@Qualifier("userServiceImplementation") Services userService) {
        this.userService = userService;
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
        user.setId(0);
        userService.saveObject(user);
        return user.toString() + "has been added";
    }

    @PutMapping("/users")
    public void updateUser(@PathVariable User user){
        userService.saveObject(user);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(int id){
        User tempUser = (User)userService.findById(id);
        if(tempUser==null){
            throw new RuntimeException("User noy found : " + id);
        }
        userService.deleteById(id);
        return tempUser.toString() + "has been deleted";
    }
}