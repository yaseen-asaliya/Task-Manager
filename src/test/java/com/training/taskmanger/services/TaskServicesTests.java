package com.training.taskmanger.services;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.repository.TaskRepository;
import com.training.taskmanger.service.TaskServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServicesTests {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskServiceImplementation service;

	@Test
	void shouldGetAllTasks() {
		List<Task> expectedTasksList = initializeListOfTasks();

		when(taskRepository.findTasksByUserId(anyInt())).thenReturn(expectedTasksList);
		assertEquals(expectedTasksList,service.getTasks(anyInt()));
	}

	@Test
	void shouldGetAllUserTasksAsPagination() {
		Pageable paging = PageRequest.of(0, 2, Sort.Direction.ASC,"start");
		List<Task> expectedTasksList = initializeListOfTasks();
		Page<Task> expectedTasksListWithPagination = new PageImpl<>(expectedTasksList);

		when(taskRepository.findTasksByUserIdWithPagination(anyInt(),any())).thenReturn(expectedTasksListWithPagination);
		assertEquals(expectedTasksListWithPagination,service.getTasks(1,paging));
	}

	@Test
	void shouldAddTask() {
		Task newTask = initializeTask();

		when(taskRepository.save(any())).thenReturn(newTask);
		assertEquals("Task saved",service.saveObject(any()));
	}

	@Test
	void shouldUpdateTask() {
		Task updatedTask = initializeTask();
		updatedTask.setDescription("do idk..."); //Do some changes...

		when(taskRepository.save(any())).thenReturn(updatedTask);
		assertEquals("Task saved",service.saveObject(any()));
	}

	@Test
	void shouldDeleteTask() {
		// Here we don't need to mock the "deleteById" cause it's a void method, so we just verify it.

		assertEquals("Task deleted",service.deleteById(anyInt()));
		verify(taskRepository).deleteById(anyInt()); //check that the method was called successfully
	}

	private Task initializeTask() {
		return new Task(1,initializeUser(),"do something...",1,"2022-05-16 08:30:10","2022-05-16 08:50:00");
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
