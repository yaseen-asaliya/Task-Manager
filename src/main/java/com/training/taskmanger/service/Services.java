package com.training.taskmanger.service;

import java.util.List;

public interface Services<T> {

    public T findById(int id);

    public void saveObject(T item);

    public void deleteById(int id);

    public List<Object> getTasks(int userId);
}
