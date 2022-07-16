package com.training.TaskManger;

import com.training.TaskManger.rest.TaskRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class TaskMangerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskMangerApplication.class, args);
		Logger logger = LoggerFactory.getLogger(TaskRestController.class.getName());

		logger.info("ss");

	}
}