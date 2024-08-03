package com.projectzero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.projectzero")
@EnableJpaRepositories(basePackages = "com.projectzero.repositories")
@EntityScan(basePackages = "com.projectzero.models")
public class ProjectZeroApp {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ProjectZeroApp.class, args);
		ProjectZeroApp app = context.getBean(ProjectZeroApp.class);
		app.checkDatabaseConnection();
	}

	public void checkDatabaseConnection() {
		try {
			jdbcTemplate.execute("SELECT 1");
			System.out.println("Database connection is healthy.");
		} catch (Exception e) {
			System.err.println("Database connection failed: " + e.getMessage());
		}
	}
}
