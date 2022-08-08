package com.training.taskmanger.services;

import com.training.taskmanger.entity.User;
import com.training.taskmanger.service.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServicesTests {

	@Mock
	private UserServiceImplementation userService;

	@Test
	void should_find_user_by_id() {
		User user = initializeUser();
		when(userService.findById(user.getId())).thenReturn(user);
		assertEquals(user,userService.findById(user.getId()));
	}

	@Test
	void should_save_user() {
		User user = initializeUser();
		when(userService.saveObject(user)).thenReturn("User saved");
		assertEquals("User saved",userService.saveObject(user));
	}

	@Test
	void should_delete_user() {
		int userId = 1;
		when(userService.deleteById(userId)).thenReturn("User deleted");
		assertEquals("User deleted",userService.deleteById(userId));
	}

	private User initializeUser() {
		User user = new User("yaseen","123","yaseens@gmail.com","ys");
		user.setId(1);
		user.setSignout(false);
		return user;
	}

}
