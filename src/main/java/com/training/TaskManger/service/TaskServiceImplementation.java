package com.training.TaskManger.service;

import com.training.TaskManger.Entity.Task;
import com.training.TaskManger.Entity.User;
import com.training.TaskManger.dao.TaskRepository;
import com.training.TaskManger.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements Services {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Object> findAll() {
        List<TaskRepository> result = taskRepository.findAll();
        return Collections.singletonList(result);
    }

    @Override
    public Object findById(int id) {
        Optional<TaskRepository> result = taskRepository.findById(id);

        Task task = null;
        if(result.isPresent()){
            task = (Task) result.get();
        }
        else{
            throw new RuntimeException("Did not find an user - id : " + id);
        }
        return task;
    }

    @Override
    public void saveObject(Object item) {
        taskRepository.save((TaskRepository)item);
    }

    @Override
    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }
}
