package com.training.taskmanger.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.http.request.LoginRequest;
import com.training.taskmanger.security.jwt.AuthTokenFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class LoginAndSignoutTests {
	@Autowired
	private MockMvc mockMvc;

	@Mock
	private UserRepository userRepository;

	@MockBean
	private AuthTokenFilter authTokenFilter;

	@Test
	void shouldSignIn() throws Exception {
		LoginRequest signinRequest = new LoginRequest("ys","123");
		String expectedBody = "{\"message\":\"Login Successfully!!\"}";

		when(authTokenFilter.getUserId()).thenReturn(1);
		when(userRepository.save(any())).thenReturn(initializeUser());

		MvcResult mvcResult = mockMvc.perform(post("/api/auth/signin")
								.contentType(MediaType.APPLICATION_JSON)
								.content(asJsonString(signinRequest)))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
	}

	@Test
	void shouldReturnUnauthorized() {

	}

	@Test
	void shouldSignoutFromAllPlaces() throws Exception {
		String expectedBody = "{\"message\":\"Signout from all places\"}";

		when(authTokenFilter.getUserId()).thenReturn(1);
		when(userRepository.save(any())).thenReturn(initializeUser());

		MvcResult mvcResult = mockMvc.perform(post("/api/auth/signoutAll"))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals(expectedBody,mvcResult.getResponse().getContentAsString());
	}

	@Test
	void shouldSignout() throws Exception {
		String expectedBody = "{\"message\":\"You've been signed out!\"}";

		MvcResult mvcResult = mockMvc.perform(post("/api/auth/signout"))
				.andExpect(status().isOk())
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

	private User initializeUser() {
		User user = new User("yaseen","123","yaseens@gmail.com","ys");
		user.setId(1);
		user.setSignout(false);
		return user;
	}

}
