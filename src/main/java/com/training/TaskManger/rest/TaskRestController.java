package com.training.TaskManger.rest;

import com.training.TaskManger.Entity.Task;
import com.training.TaskManger.dao.TaskRepository;
import com.training.TaskManger.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskRestController {

    private final Services TASK_SERVICE;

    @Autowired
    public TaskRestController(@Qualifier("taskServiceImplementation") Services taskService) {
        TASK_SERVICE = taskService;
    }

    @GetMapping("/tasks")
    public List<Object> getTasks(){
        return TASK_SERVICE.findAll();
    }

    @GetMapping("/tasks/{taskId}")
    public Object getUserTaskById(@PathVariable int taskId){
        return TASK_SERVICE.findById(taskId);
    }

    @PostMapping("/tasks")
    public String addTask(@RequestBody Task task){
        task.setUserId(0);
        TASK_SERVICE.saveObject((TaskRepository)task);
        return task.toString() + "has been added";
    }

    @PutMapping("/tasks")
    public void updateTask(@PathVariable Task task){
        TASK_SERVICE.saveObject((TaskRepository) task);
    }

    @DeleteMapping("/tasks/{userId}")
    public String deleteTask(int id){
        Task tempTask = (Task) TASK_SERVICE.findById(id);
        if(tempTask==null){
            throw new RuntimeException("Task noy found : " + id);
        }
        TASK_SERVICE.deleteById(id);
        return tempTask.toString() + "has been deleted";
    }

}
