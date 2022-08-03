package com.training.taskmanger.service;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskServiceImplementation implements Services<Task> {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImplementation.class.getName());

    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(int userId){
        return extractTasks(userId);
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

    private List<Task> extractTasks(int userId){
        List<Object> list = taskRepository.findTasksByUserId(userId);
        List<Task> tasks = new ArrayList<>();
        for (int i=0; i<list.size(); i++){
            Object[] row = (Object[]) list.get(i);
            Optional<User> user = userRepository.findById(userId);
            if (user == null) {
                throw new NotFoundException("User not found");
            }
            Task task = new Task((int) row[0],
                    user.get(),
                    (String) row[1],
                    (int) row[2],
                    (String) row[3],
                    (String) row[4]);
            System.out.println(task);
            tasks.add(task);
        }
        return tasks;

    }
}