package com.training.taskmanger.service;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements Services<Task> {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImplementation.class.getName());

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        if(taskRepository.findAll().isEmpty()){
            throw new IllegalArgumentException("No tasks available.");
        }
        LOGGER.debug("The data was token from database.");
        return taskRepository.findAll();
    }

    @Override
    public Task findById(int taskId) {
        Optional<Task> result = taskRepository.findById(taskId);

        Task tempTask = null;
        if(result.isPresent()){
            tempTask = result.get();
        }
        else{
            LOGGER.warn("Wrong id passed.");
            throw new NotFoundException("Task with id -" + taskId + "- not found.");
        }
        LOGGER.debug("The Task was token from database.");
        return tempTask;
    }

    @Override
    public void saveObject(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteById(int taskId) {
        taskRepository.deleteById(taskId);
    }



}
