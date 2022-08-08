package com.training.taskmanger.services;

import com.training.taskmanger.entity.Task;
import com.training.taskmanger.entity.User;
import com.training.taskmanger.service.TaskServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServicesTests {

	@Mock
	private TaskServiceImplementation taskService;

	@Test
	void should_get_tasks_for_current_user() {
		List<Task> expectedList = initializeExpectedList();
		when(taskService.getTasks(1)).thenReturn(expectedList);
		assertEquals(expectedList,taskService.getTasks(1));
	}

	@Test
	void should_get_tasks_with_pagination_for_current_user() {
		List<Task> expectedList = initializeExpectedList();
		Page<Task> page = new PageImpl<>(expectedList);
		Pageable paging = PageRequest.of(0, 2, Sort.Direction.ASC, String.valueOf(Sort.by("start")));
		Mockito.when(taskService.getTasks(Mockito.anyInt(), any())).thenReturn(page);
		assertEquals(page,taskService.getTasks(1,paging));
	}

	@Test
	void should_get_taskById() {
		User user = initializeUser();
		Task task = new Task(1, user, "do something....",1,"2022-03-09 08:30:10", "2022-03-09 08:50:00");
		when(taskService.findById(task.getId())).thenReturn(task);
		assertEquals(task,taskService.findById(task.getId()));
	}

	@Test
	void should_save_task() {
		User user = initializeUser();
		Task task = new Task(4, user, "t2",1,"2022-03-09 08:30:10", "2022-03-09 08:50:00");
		when(taskService.saveObject(task)).thenReturn("Task saved");
		assertEquals("Task saved",taskService.saveObject(task));
	}

	@Test
	void should_delete_task() {
		int userId = 1;
		when(taskService.deleteById(userId)).thenReturn("Task deleted");
		assertEquals("Task deleted",taskService.deleteById(userId));
	}

	private List<Task> initializeExpectedList(){
		List<Task> expectedList = new ArrayList<>();
		User user = initializeUser();
		expectedList.add(new Task(1, user, "t2",1,"2022-03-09 08:30:10", "2022-03-09 08:50:00"));
		expectedList.add(new Task(2, user, "t2",1,"2022-03-10 08:30:10", "2022-03-10 08:50:00"));
		expectedList.add(new Task(3, user, "t2",1,"2022-03-15 08:30:10", "2022-03-15 08:50:00"));
		return expectedList;
	}

	private User initializeUser() {
		User user = new User("yaseen","123","yaseens@gmail.com","ys");
		user.setId(1);
		user.setSignout(false);
		return user;
	}
}
