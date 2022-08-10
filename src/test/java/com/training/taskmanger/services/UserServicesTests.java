package com.training.taskmanger.services;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.UserRepository;
import com.training.taskmanger.service.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServicesTests {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImplementation service;

	@Test
	void shouldGetCurrentUser() {
		User user = initializeUser();
		int userId =1;

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		service.findById(userId);
		assertEquals(user,userRepository.findById(userId).get());
	}

	@Test
	void shouldUpdateUser() {
		User user = initializeUser();
		user.setEmail("yaseen.asaliya22@gmail.com"); // Make update

		when(userRepository.save(user)).thenReturn(user);
		service.saveObject(user);
		assertEquals(user,userRepository.save(user));
	}

	@Test
	void shouldDeleteUser() {
		User user = initializeUser();

		service.deleteById(user.getId());
		Iterable getUser = userRepository.findAllById(Collections.singleton(user.getId()));
		assertThat(getUser);
	}

	@Test
	public void shouldLoadUserByUsername() {
		User user = initializeUser();
		Optional<User> opt = Optional.of(user);
		when(userRepository.findByUsername(anyString())).thenReturn(opt);
		UserDetails userDetails = service.loadUserByUsername(anyString());
		assertEquals("ys", userDetails.getUsername());
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
