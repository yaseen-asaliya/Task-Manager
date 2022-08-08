package com.training.taskmanger.service;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskServiceImplementation implements Services<Task> {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImplementation.class.getName());

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Override
    public List<Task> getTasks(int userId){
        return taskRepository.findTasksByUserId(userId);
    }

    @Override
    public Page<Task> getTasks(int userId, Pageable pageable) {
        return taskRepository.findTasksByUserIdWithPagination(userId,pageable);
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
    public String saveObject(Task task) {
        taskRepository.save(task);
        return "Task saved";
    }

    @Override
    public String deleteById(int taskId) {
        taskRepository.deleteById(taskId);
        return "Task deleted";
    }
}