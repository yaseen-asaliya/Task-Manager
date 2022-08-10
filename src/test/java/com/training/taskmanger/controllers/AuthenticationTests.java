package com.training.taskmanger.controllers;

import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.security.WebSecurityConfig;
import com.training.taskmanger.security.http.request.LoginRequest;
import com.training.taskmanger.security.http.request.SignupRequest;
import com.training.taskmanger.service.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsImpl userDetails;

	@Test
	void should_signin() {
		LoginRequest request = new LoginRequest("ys","123");
		ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);

		/*when(authenticationController.authenticateUser(request)).thenReturn(response);
		userDetails.
		assertEquals(response,authenticationController.authenticateUser(request));*/
	}

	/*@Test
	void should_signup() {
		SignupRequest request = new SignupRequest("yaseen","123","yaseen@test.com","ys");
		ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);
		when(authenticationController.registerUser(request)).thenReturn(response);
		assertEquals(response,authenticationController.registerUser(request));
	}

	@Test
	void should_Logout() {
		ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);
		when(authenticationController.logoutUser()).thenReturn(response);
		assertEquals(response,authenticationController.logoutUser());
	}

	@Test
	void should_logout_from_all_places() {
		ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);
		when(authenticationController.logoutAll()).thenReturn(response);
		assertEquals(response,authenticationController.logoutAll());
	}*/
}
