package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.service.Services;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskRestController {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class.getName());
    private Services taskService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    public TaskRestController(@Qualifier("taskServiceImplementation") Services taskService) {
        this.taskService = taskService;
        LOGGER.info("Task Controller created successfully");
    }

    // Get all tasks for current user
    @GetMapping("/tasks")
    public List<Object> getAllUserTasks(){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        if(taskService.getTasks(userId).size() == 0){
            throw new NotFoundException("No tasks available");
        }
        return taskService.getTasks(userId);
    }

    // Add task for current user
    @PostMapping("/tasks")
    public String addTask(@RequestBody Task task){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User with id -" + userId +  "- not found.");
        }
        task.setUser(optionalUser.get());
        taskService.saveObject(task);
        LOGGER.debug("Task has been posted.");
        return task + " added successfully.";
    }

    // Update tasks for current user
    @PutMapping("/tasks")
    public String updateTask(@RequestBody Task task){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        Optional<Task> tempTask =taskRepository.findById(task.getId());
        if(tempTask == null){
            LOGGER.warn("Wrong user id passed");
            throw new NotFoundException("Task with id -" + task.getId() + "- not found.");
        }

        if(tempTask.get().getUser().getId() != userId){
            throw new NotFoundException("This task is not belong to you.");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new NotFoundException("User with id -" + userId +  "- not found.");
        }
        optionalUser.get().setId(userId);
        task.setUser(optionalUser.get());

        taskService.saveObject(task);
        LOGGER.debug("Task updated completed.");
        return task + " updated successfully.";
    }

    // Delete current user tasks
    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable int taskId){
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        Task tempTask = (Task) taskService.findById(taskId);
        if(tempTask == null){
            LOGGER.warn("Wrong user id passed");
            throw new NotFoundException("Task with id -" + taskId + "- not found.");
        }

        if(tempTask.getUser().getId() != userId){
            throw new NotFoundException("This task is not belong to you.");
        }

        taskService.deleteById(taskId);
        LOGGER.debug("Task deleted completed.");
        return tempTask + " deleted successfully.";
    }

    private boolean isSignout() {
        int userId = authTokenFilter.getUserId();
        Optional<User> user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        if (user.get().getSignout()) {
            return true;
        }
        return false;
    }

    private void checkIfLogin(){
        if(isSignout()){
            throw new RuntimeException("You're unauthorized");
        }
    }
}