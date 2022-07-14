package com.training.TaskManger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = {"com.training.TaskManger"})
public class TaskMangerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskMangerApplication.class, args);
	}
}