package com.training.TaskManger.dao;

import com.training.TaskManger.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Integer> {
    
}
