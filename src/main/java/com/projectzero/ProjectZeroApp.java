package com.projectzero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = "com.projectzero")
@EnableJpaRepositories(basePackages = "com.projectzero.repositories")
@EntityScan(basePackages = "com.projectzero.models")
public class ProjectZeroApp {
	public static void main(String[] args) {
		SpringApplication.run(ProjectZeroApp.class, args);
	}
}
