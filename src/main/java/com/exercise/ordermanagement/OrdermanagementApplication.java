package com.exercise.ordermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class OrdermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdermanagementApplication.class, args);
	}

}
