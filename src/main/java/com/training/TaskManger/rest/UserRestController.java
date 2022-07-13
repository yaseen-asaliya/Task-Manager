package com.training.TaskManger.rest;

import com.training.TaskManger.Entity.User;
import com.training.TaskManger.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final Services USER_SERVICE;

    @Autowired
    public UserRestController(@Qualifier("userServiceImplementation") Services userService) {
        USER_SERVICE = userService;
    }

    @GetMapping("/users")
    public List<Object> getUsers(){
        return USER_SERVICE.findAll();
    }

    @GetMapping("/users/{userId}")
    public Object getUserById(@PathVariable int userId){
        return USER_SERVICE.findById(userId);
    }

    @PostMapping("/users")
    public String addUser(@RequestBody User user){
        user.setId(0);
        USER_SERVICE.saveObject(user);
        return user.toString() + "has been added";
    }

    @PutMapping("/users")
    public void updateUser(@PathVariable User user){
        USER_SERVICE.saveObject(user);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(int id){
        User tempUser = (User)USER_SERVICE.findById(id);
        if(tempUser==null){
            throw new RuntimeException("Employee noy found : " + id);
        }
        USER_SERVICE.deleteById(id);
        return tempUser.toString() + "has been deleted";
    }
}