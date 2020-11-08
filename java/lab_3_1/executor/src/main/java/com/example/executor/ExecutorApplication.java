package com.example.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ExecutorApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ExecutorApplication.class);
		// app.setDefaultProperties(Collections.singletonMap("server.port", "6491"));
		app.run(args);
	}

}
