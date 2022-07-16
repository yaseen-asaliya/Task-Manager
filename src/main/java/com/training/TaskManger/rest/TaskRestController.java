package com.training.TaskManger.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        if(taskService == null){
            throw new NullPointerException();
        }
        this.taskService = taskService;
        LOGGER.info("Task Controller created successfully");
    }

    @GetMapping("/tasks")
    public List<Object> getTasks(){
        return taskService.findAll();
    }

    @GetMapping("/tasks/{taskId}")
    public Object getTaskById(@PathVariable int taskId){

        Task task = (Task) taskService.findById(taskId);
        if(task == null){
            throw new RuntimeException("Employee not found : " + taskId);
        }

        return task;
    }

    @PostMapping("/tasks")
    public String addTask(@RequestBody Task task){
        task.setUserId(0);
        taskService.saveObject(task);
        return task.toString() + "has been added";
    }

    @PutMapping("/tasks")
    public void updateTask(@PathVariable Task task){
        taskService.saveObject(task);
    }

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(int taskId){
        Task tempTask = (Task) taskService.findById(taskId);
        if(tempTask==null){
            throw new RuntimeException("Task noy found : " + taskId);
        }
        taskService.deleteById(taskId);
        return tempTask.toString() + "has been deleted";
    }


}
