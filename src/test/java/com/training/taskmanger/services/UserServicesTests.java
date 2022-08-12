package com.training.taskmanger.services;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.service.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServicesTests {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImplementation service;

	@Test
	void shouldGetCurrentUser() {
		User expectedUser = initializeUser();

		when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));
		assertEquals(expectedUser,service.findById(anyInt()));
	}

	@Test
	void shouldUpdateUser() {
		User updatedUser = initializeUser();
		updatedUser.setEmail("yaseen.asaliya22@gmail.com"); // Do some changes...

		when(userRepository.save(any())).thenReturn(updatedUser);
		assertEquals("User saved",service.saveObject(any()));
	}

	@Test
	void shouldDeleteUser() {
		// Here we don't need to mock the "deleteById" cause it's a void method, so we just verify it.

		assertEquals("User deleted",service.deleteById(anyInt()));
		verify(userRepository).deleteById(anyInt()); //check that the method was called successfully
	}

	@Test
	public void shouldLoadUserByUsername() {
		Optional<User> expectedUser = Optional.of(initializeUser());

		when(userRepository.findByUsername(anyString())).thenReturn(expectedUser);
		assertEquals(expectedUser.get().getUsername(), service.loadUserByUsername(anyString()).getUsername());
	}

	@Test
	void unimplementedMethods() {
		service.getTasks(1);
		service.getTasks(1,null);
	}

	private User initializeUser() {
		User user = new User("yaseen","123","yaseens@gmail.com","ys");
		user.setId(1);
		user.setSignout(false);
		return user;
	}
}
