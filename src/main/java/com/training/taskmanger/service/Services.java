package com.training.taskmanger.service;

import com.training.taskmanger.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface Services<T> {

    public T findById(int id);

    public String saveObject(T item);

    public String deleteById(int id);

    public List<Task> getTasks(int userId);
    public Page<Task> getTasks(int userId, Pageable pageable);
}
