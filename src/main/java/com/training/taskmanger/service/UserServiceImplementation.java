package com.training.taskmanger.service;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.controllers.TaskRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements Services<User> {

    public final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class.getName());
    private UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        if(userRepository.findAll().isEmpty()){
            throw new IllegalArgumentException("No users available.");
        }
        LOGGER.debug("The data was token from database.");
        return userRepository.findAll();
    }

    @Override
    public User findById(int userId) {
        Optional<User> result = userRepository.findById(userId);

        User user = null;
        if(result.isPresent()){
            user = result.get();
        }
        else{
            LOGGER.warn("Wrong id passed.");
            throw new NotFoundException("User with id -" + userId + "- not found.");
        }
        LOGGER.debug("The User was token from database.");
        return user;
    }

    @Override
    public void saveObject(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(int userId) {
        userRepository.deleteById(userId);
    }



}
