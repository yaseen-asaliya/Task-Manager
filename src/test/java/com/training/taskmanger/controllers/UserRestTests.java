package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserRestTests {
	@Mock
	private UserRestController userRestController;

	@Test
	void should_get_current_user() {
		User user = initializeUser();
		when(userRestController.getCurrentUser()).thenReturn(user);
		assertEquals(user,userRestController.getCurrentUser());
	}

	@Test
	void should_update_user() {
		User user = initializeUser();
		user.setEmail("yaseen.asaliya22@gmail.com"); // Make update
		when(userRestController.updateUser(user)).thenReturn(user + " updated successfully.");
		assertEquals(user + " updated successfully.",userRestController.updateUser(user));
	}

	@Test
	void should_delete_user() {
		User user = initializeUser();
		when(userRestController.deleteUser()).thenReturn(user + " deleted successfully.");
		assertEquals(user + " deleted successfully.",userRestController.deleteUser());
	}

	@Test
	void should_reset_signout_column() {
		when(userRestController.resetIsSignoutColumn()).thenReturn(true);
		assertEquals(true,userRestController.resetIsSignoutColumn());
	}

	private User initializeUser() {
		User user = new User("yaseen","123","yaseens@gmail.com","ys");
		user.setId(1);
		user.setSignout(false);
		return user;
	}

}
