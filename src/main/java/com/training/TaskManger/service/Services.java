package com.training.TaskManger.service;

import com.training.TaskManger.Entity.Task;

import java.util.List;

public interface Services<T> {
    public List<T> findAll();

    public T findById(int id);

    public void saveObject(T item);

    public void deleteById(int id);
}
