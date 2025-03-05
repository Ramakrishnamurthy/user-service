package com.note.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.data.jpa.repository.config.*;

@SpringBootApplication(scanBasePackages = "com.note.security")
@EnableJpaRepositories(basePackages = "com.note.security.dao")
@EntityScan(basePackages = "com.note.security.dao.model")
public class SecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
}