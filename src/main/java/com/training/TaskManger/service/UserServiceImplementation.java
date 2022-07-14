package com.training.TaskManger.service;

import com.training.TaskManger.Entity.User;
import com.training.TaskManger.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Component("UserService")
public class UserServiceImplementation implements Services {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<Object> findAll() {
        List<User> result = userRepository.findAll();
        return Collections.singletonList(result);
    }

    @Override
    public Object findById(int id) {
        Optional<User> result = userRepository.findById(id);

        User user = null;
        if(result.isPresent()){
            user = (User) result.get();
        }
        else{
            throw new RuntimeException("Did not find an user - id : " + id);
        }
        return user;
    }

    @Override
    public void saveObject(Object item) {
        userRepository.save((User) item);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
