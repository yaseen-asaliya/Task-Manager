package com.training.taskmanger.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import com.training.taskmanger.service.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.training.taskmanger.controllers.LoginAndSignoutTests.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImplementation userService;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder encoder;

    @BeforeEach
    private void commonMocks() {
        when(authTokenFilter.getUserId()).thenReturn(1);
        when(userService.findById(anyInt())).thenReturn(initializeUser());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(initializeUser()));

    }

    @Test
    void shouldGetCurrentUser() throws Exception {
        User expectedUser = initializeUser();

        MvcResult mvcResult = mockMvc.perform(get("/api/user")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(expectedUser)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedUser.toString(),castingToUser(mvcResult.getResponse().getContentAsString()).toString());
    }

    @Test
    void shouldNotGetSignoutUser() throws Exception {
        User expectedUser = initializeUser();
        expectedUser.setSignout(true);

        when(authTokenFilter.getUserId()).thenReturn(1);
        when(userService.findById(anyInt())).thenReturn(expectedUser);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));

        MvcResult mvcResult = mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedUser)))
                .andReturn();

        assertEquals(true,mvcResult.getResponse().getContentAsString().contains("You're unauthorized"));
    }

    @Test
    void shouldNotGetNotFound() throws Exception {
        User expectedUser = initializeUser();
        expectedUser.setSignout(true);

        when(authTokenFilter.getUserId()).thenReturn(1);
        when(userService.findById(anyInt())).thenReturn(expectedUser);
        when(userRepository.findById(anyInt())).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedUser)))
                .andReturn();

        assertEquals(true,mvcResult.getResponse().getContentAsString().contains("User not found"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User expectedUser = initializeUser();
        expectedUser.setEmail("Yaseen@gmail.com");
        expectedUser.setPassword(encoder.encode(expectedUser.getPassword()));
        String expectedBody = expectedUser + " updated successfully.";

        when(userService.saveObject(any())).thenReturn(String.valueOf(expectedUser));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));
        when(userService.saveObject(any())).thenReturn(String.valueOf(expectedUser));

        MvcResult mvcResult = mockMvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedUser)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        User tempUser = initializeUser();
        String expectedBody = tempUser + " deleted successfully.";

        when(userService.deleteById(anyInt())).thenReturn("User deleted");

        MvcResult mvcResult = mockMvc.perform(delete("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tempUser)))
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

    private User castingToUser(String body) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(body, User.class);
        return user;
    }
}
