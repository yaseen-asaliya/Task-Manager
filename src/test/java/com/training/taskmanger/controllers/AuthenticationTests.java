package com.training.taskmanger.controllers;

import com.training.taskmanger.security.http.request.LoginRequest;
import com.training.taskmanger.security.http.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationTests {

	@Mock
	private AuthenticationController authenticationController;

	@Test
	void should_signin() {
		LoginRequest request = new LoginRequest("ys","123");
		ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);
		when(authenticationController.authenticateUser(request)).thenReturn(response);
		assertEquals(response,authenticationController.authenticateUser(request));
	}

	@Test
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
	}
}
