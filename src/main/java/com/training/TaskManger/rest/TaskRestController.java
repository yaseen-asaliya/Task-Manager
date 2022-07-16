package com.training.TaskManger.rest;

import com.training.TaskManger.Entity.Task;
import com.training.TaskManger.service.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskRestController {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class.getName());
    private Services taskService;

    public TaskRestController(){

    }
    @Autowired
    public TaskRestController(@Qualifier("taskServiceImplementation") Services taskService) {
        this.taskService = taskService;
        LOGGER.info("Task Controller created successfully");
    }

    @GetMapping("/tasks")
    public List<Object> getTasks(){
        return taskService.findAll();
    }

    @GetMapping("/tasks/{taskId}")
    public Object getTaskById(@PathVariable int taskId){
        return taskService.findById(taskId);
    }

    @PostMapping("/tasks")
    public String addTask(@RequestBody Task task){
        if(task == null){
            throw new NullPointerException("Task is null");
        }
        task.setUserId(0);
        taskService.saveObject(task);
        LOGGER.debug("Task posted completed.");
        return task.toString() + " added successfully.";
    }

    @PutMapping("/tasks")
    public String updateTask(@RequestBody Task task){
        if(task == null){
            throw new NullPointerException("Task is null");
        }
        taskService.saveObject(task);
        LOGGER.debug("Task updated completed.");
        return task.toString() + " updated successfully.";
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable int taskId){
        Task tempTask = (Task) taskService.findById(taskId);
        if(tempTask == null){
            LOGGER.warn("Wrong user id passed");
            throw new NotFoundException("Task with id -" + taskId + "- not found.");
        }
        taskService.deleteById(taskId);
        LOGGER.debug("Task deleted completed.");
        return tempTask.toString() + " deleted successfully.";
    }
}
