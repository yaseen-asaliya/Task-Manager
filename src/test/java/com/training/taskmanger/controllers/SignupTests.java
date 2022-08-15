package com.training.taskmanger.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.http.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class SignupTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldSignUp() throws Exception {
        SignupRequest signupRequest = new SignupRequest("yaseen","123","yaseen2@test.com","yss");
        String expectedBody = "{\"message\":\"User registered successfully!\"}";
        statusUsernameAndEmail(false,false);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldReturnExistUsername() throws Exception {
        SignupRequest signupRequest = new SignupRequest("yaseen","123","yaseen2@test.com","yss");
        String expectedBody = "{\"message\":\"Error: Username is already taken!\"}";
        statusUsernameAndEmail(true,false);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    @Test
    void shouldReturnExistEmail() throws Exception {
        SignupRequest signupRequest = new SignupRequest("yaseen","123","yaseen2@test.com","yss");
        String expectedBody = "{\"message\":\"Error: Email is already in use!\"}";
        statusUsernameAndEmail(false,true);

        MvcResult mvcResult = mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void statusUsernameAndEmail(boolean usernameStatus,boolean emailStatus) {
        given(userRepository.existsByUsername(anyString())).willReturn(usernameStatus);
        given(userRepository.existsByEmail(anyString())).willReturn(emailStatus);
    }
}
