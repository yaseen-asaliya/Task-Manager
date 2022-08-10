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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServicesTests {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskServiceImplementation service;

	@Test
	void shouldGetAllTasks() {
		User user = initializeUser();
		List<Task> taskList = initializeListOfTasks();

		when(taskRepository.findAll()).thenReturn(taskList);
		service.getTasks(user.getId());
		assertEquals(taskList,taskRepository.findAll());
	}

	@Test
	void shouldGetAllUserTasksAsPagination() {
		Pageable paging = PageRequest.of(0, 2, Sort.Direction.ASC,"start");
		List<Task> expectedList = initializeListOfTasks();
		Page<Task> listOfTasks = new PageImpl<>(expectedList);

		when(taskRepository.findTasksByUserIdWithPagination(1,paging)).thenReturn(listOfTasks);
		service.getTasks(1,paging);
		assertEquals(listOfTasks,taskRepository.findTasksByUserIdWithPagination(1,paging));
	}

	@Test
	void shouldAddTask() {
		User user = initializeUser();
		Task task = new Task(3,user,"do something...",1,"2022-05-20 08:30:10","2022-05-20 08:50:00");

		when(taskRepository.save(task)).thenReturn(task);
		service.saveObject(task);
		assertEquals(task,taskRepository.save(task));
	}

	@Test
	void shouldUpdateTask() {
		User user = initializeUser();
		Task newTask = new Task(3,user,"do something...",1,"2022-05-20 08:30:10","2022-05-20 08:50:00");
		newTask.setDescription("do idk..."); //Do changes...

		when(taskRepository.save(newTask)).thenReturn(newTask);
		service.saveObject(newTask);
		assertEquals(newTask,taskRepository.save(newTask));
	}

	@Test
	void shouldDeleteTask() {
		User user = initializeUser();
		Task task = new Task(3,user,"do something...",1,"2022-05-20 08:30:10","2022-05-20 08:50:00");

		service.deleteById(task.getId());
		Iterable getTask = taskRepository.findAllById(Collections.singleton(task.getId()));
		assertThat(getTask);
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
