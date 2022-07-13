package com.training.TaskManger.service;

import java.util.List;

public interface Services {
    public List<Object> findAll();

    public Object findById(int id);

    public void saveObject(Object item);

    public void deleteById(int id);
}
