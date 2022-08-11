package com.training.taskmanger.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.security.WebSecurityConfig;
import com.training.taskmanger.service.TaskServiceImplementation;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Security;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TaskControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImplementation service;

    // Noy ready
    /*@Test
    @WithMockUser(username = "ys",password = "123")
    void shouldGetAllTasks() throws Exception {
        Optional<Integer> page = Optional.of(0);
        Optional<String> sortBy = Optional.of("start");
        Optional<Integer> pageSize = Optional.of(0);
        Optional<String> sortDirection = Optional.of("ASC");

        MultiValueMap<String, Object> paraMap =new LinkedMultiValueMap<>();
        paraMap.add("page", page);
        paraMap.add("sortBy", sortBy);
        paraMap.add("pageSize", pageSize);
        paraMap.add("sortDirection", sortDirection);

        URI uri = UriComponentsBuilder.fromPath("/api/tasks")
                .queryParam("page", Optional.of(1))
                .queryParam("sortBy",Optional.of("start"))
                .queryParam("pageSize", Optional.of(3))
                .queryParam("sortDirection",Optional.of("ASC"))
                .build().toUri();

        mockMvc.perform( MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "ys",password = "123")
    void shouldAddTask() throws Exception {
        Task task = initializeTask();

        JSONObject json = new JSONObject();
        json.put("name", "student");

        String x = "{\"id\":1,\"user\":{\"id\":1,\"name\":\"yaseen\"," +
                "\"password\":\"$2a$10$O27NNHkM8h.e458Dx4EET./CY4iism9/iC0yYgglhU6d2HdiamaAy\"," +
                "\"email\":\"yaseens@gmail.com\",\"username\":\"ys\",\"signout\":false}," +
                "\"description\":\"dosomething....\",\"completed\":1,\"start\":\"2022-05-1608:30:10\"" +
                ",\"finish\":\"2022-05-1608:50:00\"";

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(x))
                .andExpect(status().isOk());

    }*/

    @Test
    @WithMockUser(username = "ys",password = "123")
    void shouldDeleteTask() throws Exception {
        int taskId = 1;
        when(service.deleteById(taskId)).thenReturn(initializeTask() + " deleted successfully.");
        mockMvc.perform(delete("/api/tasks/{taskId}",taskId)).andExpect(status().isAccepted());
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

}
