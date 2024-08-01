package com.projectzero.ProjectZero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.projectzero")
@EntityScan("com.revature.models")
@EnableJpaRepositories("com.revature.repositories")
public class ProjectZeroApp {

	public static void main(String[] args) {
		SpringApplication.run(ProjectZeroApp.class, args);
	}

}
