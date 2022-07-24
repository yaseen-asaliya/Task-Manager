package com.training.taskmanger.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface Services<T> {
    public List<T> findAll();

    public T findById(int id);

    public void saveObject(T item);

    public void deleteById(int id);

}
