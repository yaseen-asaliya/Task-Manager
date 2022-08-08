package com.training.taskmanger.controllers;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class TaskRestTests {

	@Mock
	private TaskRestController taskRestController;

	@Test
	void should_get_all_user_tasks_as_pagination() {
		Optional<Integer> page = Optional.of(0);
		Optional<String> sortBy = Optional.of("start");
		Optional<Integer> pageSize = Optional.of(2);
		Optional<String> sortDirection = Optional.of("ASC");

		List<Task> expectedList = initializeListOfTasks();
		Page<Task> listOfTasks = new PageImpl<>(expectedList);
		when(taskRestController.getAllUserTasks(page,sortBy,pageSize,sortDirection)).thenReturn(listOfTasks);
		assertEquals(listOfTasks,taskRestController.getAllUserTasks(page,sortBy,pageSize,sortDirection));
	}

	@Test
	void should_add_task() {
		User user = initializeUser();
		Task task = new Task(3,user,"do something...",1,"2022-05-20 08:30:10","2022-05-20 08:50:00");
		when(taskRestController.addTask(task)).thenReturn(task + " added successfully.");
		assertEquals(task + " added successfully.",taskRestController.addTask(task));
	}

	@Test
	void should_update_task() {
		User user = initializeUser();
		Task newTask = new Task(3,user,"do something...",1,"2022-05-20 08:30:10","2022-05-20 08:50:00");
		newTask.setDescription("do idk..."); //Do changes...
		when(taskRestController.updateTask(newTask)).thenReturn(newTask + " updated successfully.");
		assertEquals(newTask + " updated successfully.",taskRestController.updateTask(newTask));
	}

	@Test
	void should_delete_task() {
		User user = initializeUser();
		Task task = new Task(3,user,"do something...",1,"2022-05-20 08:30:10","2022-05-20 08:50:00");
		when(taskRestController.deleteTask(task.getId())).thenReturn(task + " deleted successfully.");
		assertEquals(task + " deleted successfully.",taskRestController.deleteTask(task.getId()));
	}

	private List<Task> initializeListOfTasks() {
		List<Task> taskList = new ArrayList<>();
		User user = initializeUser();
		taskList.add(new Task(1,user,"do something...",1,"2022-05-16 08:30:10","2022-05-16 08:50:00"));
		taskList.add(new Task(2,user,"do something...",0,"2022-05-17 08:30:10","2022-05-17 08:50:00"));
		return taskList;
	}

	private User initializeUser() {
		User user = new User("yaseen","123","yaseens@gmail.com","ys");
		user.setId(1);
		user.setSignout(false);
		return user;
	}


}
