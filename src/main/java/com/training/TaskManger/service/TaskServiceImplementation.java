package com.training.TaskManger.service;

import com.training.TaskManger.Entity.Task;
import com.training.TaskManger.dao.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements Services<Task> {

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();

    }

    @Override
    public Task findById(int id) {
        Optional<Task> result = taskRepository.findById(id);

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
    public void saveObject(Task item) {
        taskRepository.save(item);
    }

    @Override
    public void deleteById(int id) {
        taskRepository.deleteById(id);
    }
}
