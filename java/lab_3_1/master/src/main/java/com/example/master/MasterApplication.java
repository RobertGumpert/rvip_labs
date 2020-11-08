package com.example.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class MasterApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MasterApplication.class);
        app.run(args);
    }

}
