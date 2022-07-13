package com.training.TaskManger.rest;

import com.training.TaskManger.Entity.Task;
import com.training.TaskManger.dao.TaskRepository;
import com.training.TaskManger.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskRestController {
    private Services taskService;

    @Autowired
    public TaskRestController(@Qualifier("taskServiceImplementation") Services taskService) {
        this.taskService = taskService;
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
        taskService.saveObject((TaskRepository) task);
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
