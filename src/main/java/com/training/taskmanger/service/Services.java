package com.training.taskmanger.service;

import com.training.taskmanger.entity.Task;

import java.util.List;

public interface Services<T> {

    public T findById(int id);

    public void saveObject(T item);

    public void deleteById(int id);

    public List<Task> getTasks(int userId);
}
