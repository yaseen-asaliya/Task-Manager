package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.exception.ErrorResponseException;
import com.training.taskmanger.exception.NotFoundException;
import com.training.taskmanger.exception.RestExceptionHandler;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.service.TaskServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static com.training.taskmanger.controllers.LoginAndSignoutTests.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.data.domain.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImplementation taskService;

    @Mock
    private TaskRepository taskRepository;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "ys",password = "123")
    void shouldGetAllTasks() throws Exception {
        MultiValueMap<String, Object> paraMap = getParaMap();
        URI uri = getUrl();
        Page<Task> expectedTasksListWithPagination = new PageImpl<>(initializeListOfTasks());

        when(authTokenFilter.getUserId()).thenReturn(1);
        when(taskService.getTasks(anyInt())).thenReturn(initializeListOfTasks());
        when(taskService.getTasks(anyInt(),any())).thenReturn(expectedTasksListWithPagination);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(initializeUser()));

        MvcResult mvcResult = mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(paraMap)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(asJsonString(expectedTasksListWithPagination),mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "ys",password = "123")
    void shouldAddTask() throws Exception {
        Task newTask = initializeTask();
        String expectedBody = newTask + " added successfully.";
        when(authTokenFilter.getUserId()).thenReturn(1);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(initializeUser()));
        when(taskService.getTasks(anyInt())).thenReturn(initializeListOfTasks());

        MvcResult mvcResult = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "ys",password = "123")
    void shouldUpdateTask() throws Exception {
        Task newTask = initializeTask();
        newTask.setCompleted(0); // Do some changes...
        String expectedBody = newTask + " updated successfully.";

        when(authTokenFilter.getUserId()).thenReturn(1);
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(newTask));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(initializeUser()));
        when(taskService.saveObject(any())).thenReturn(String.valueOf(newTask));
        when(taskService.getTasks(anyInt())).thenReturn(initializeListOfTasks());

        MvcResult mvcResult = mockMvc.perform(put("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newTask)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "ys",password = "123")
    void shouldDeleteTask() throws Exception {
        String expectedBody = initializeTask() + " deleted successfully.";

        when(authTokenFilter.getUserId()).thenReturn(1);
        when(taskService.findById(anyInt())).thenReturn(initializeTask());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(initializeUser()));

        MvcResult mvcResult = mockMvc.perform(delete("/api/tasks/{taskId}",1))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    private User initializeUser() {
        User user = new User("yaseen","123","yaseens@gmail.com","ys");
        user.setId(1);
        user.setSignout(false);
        return user;
    }

    private Task initializeTask() {
        Task task = new Task(1,initializeUser(),"test",1,"2022-05-16 08:30:10","2022-05-16 08:50:00");
        return task;
    }

    private List<Task> initializeListOfTasks() {
        List<Task> taskList = new ArrayList<>();
        User user = initializeUser();
        taskList.add(new Task(1,user,"do something...",1,"2022-05-16 08:30:10","2022-05-16 08:50:00"));
        taskList.add(new Task(2,user,"do something...",0,"2022-05-17 08:30:10","2022-05-17 08:50:00"));
        return taskList;
    }

    private URI getUrl() {
        MultiValueMap<String, Object> paraMap = new LinkedMultiValueMap<>();
        paraMap.add("page", Optional.of(0));
        paraMap.add("sortBy", Optional.of("start"));
        paraMap.add("pageSize", Optional.of(2));
        paraMap.add("sortDirection", Optional.of("ascending"));
        URI uri = UriComponentsBuilder.fromPath("/api/tasks")
                .queryParam("page", Optional.of(1))
                .queryParam("sortBy",Optional.of("start"))
                .queryParam("pageSize", Optional.of(3))
                .queryParam("sortDirection",Optional.of("ascending"))
                .build().toUri();
        return uri;
    }

    private MultiValueMap<String, Object> getParaMap() {
        MultiValueMap<String, Object> paraMap = new LinkedMultiValueMap<>();
        paraMap.add("page", Optional.of(0));
        paraMap.add("sortBy", Optional.of("start"));
        paraMap.add("pageSize", Optional.of(2));
        paraMap.add("sortDirection", Optional.of("ascending"));
        return paraMap;
    }

}
