package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.service.Services;
import com.training.taskmanger.service.TaskServiceImplementation;
import com.training.taskmanger.timeconflict.TimeConflict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api")
public class TaskRestController {
    public final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class.getName());
    private final int FIRST_PAGE = 0;
    private final String DEFAULT_SORT = "start";
    private final String ASCENDING_DIRECTION = "ascending";
    private final String DESCENDING_DIRECTION = "descending";
    private final int EMPTY_LIST = 0;
    private final int DEFAULT_PAGE_SIZE = 3;
    private Services taskService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private TaskServiceImplementation taskServiceImplementation;

    @Autowired
    public TaskRestController(@Qualifier("taskServiceImplementation") Services taskService) {
        this.taskService = taskService;
        LOGGER.info("Task Controller created successfully");
    }

    // Get all tasks for current user
    @GetMapping("/tasks")
    public Page<Task> getAllUserTasks(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Integer> pageSize,
            @RequestParam Optional<String> sortDirection
    ) {
        checkIfLogin();
        int userId = authTokenFilter.getUserId();
        if(taskService.getTasks(userId).size() == EMPTY_LIST){
            throw new NotFoundException("No tasks available");
        }
        Sort.Direction sort = getDirection(sortDirection);

        Pageable paging = PageRequest
                .of(page.orElse(FIRST_PAGE), pageSize.orElse(DEFAULT_PAGE_SIZE), sort,sortBy.orElse(DEFAULT_SORT));
        return taskService.getTasks(userId,paging);
    }

    // Add task for current user
    @PostMapping("/tasks")
    public String addTask(@RequestBody Task task)  {
        checkIfLogin();
        checkConflict(task);
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
        checkConflict(task);
        int userId = authTokenFilter.getUserId();
        Optional<Task> tempTask = taskRepository.findById(task.getId());
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

    private void checkConflict(Task task){
        try {
            int userId = authTokenFilter.getUserId();
            TimeConflict timeConflict = new TimeConflict(taskServiceImplementation);
            if(timeConflict.isConflict(task.getStart(), task.getFinish(),userId,task.getId()) == true){
                throw new RuntimeException("Conflict between tasks times.");
            }
        } catch (Exception exp){
            exp.getStackTrace();
            throw new RuntimeException("Conflict between tasks times.");
        }
    }
    private Sort.Direction getDirection(Optional<String> sortDirection) {
        Sort.Direction sort;
        if(sortDirection.get().equals(ASCENDING_DIRECTION)){
            sort = ASC;
        }
        else if (sortDirection.get().equals(DESCENDING_DIRECTION)){
            sort = DESC;
        }
        else {
            throw new NotFoundException("Wrong direction passed.");
        }
        return sort;
    }
}