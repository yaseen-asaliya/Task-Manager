package com.training.TaskManger.service;

import com.training.TaskManger.entity.User;
import com.training.TaskManger.dao.UserRepository;
import com.training.TaskManger.exception.NotFoundException;
import com.training.TaskManger.rest.TaskRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements Services<User> {

    public final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class.getName());
    private UserRepository userRepository;

    @Autowired
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
