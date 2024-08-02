package com.projectzero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.projectzero")
public class ProjectZeroApp {
	public static void main(String[] args) {
		SpringApplication.run(ProjectZeroApp.class, args);
	}
}
