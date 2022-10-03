package com.sangboyoon.accounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJpaAuditing
public class AccounterApplication {

	@GetMapping("/")
	public String hello() {
		return "Hello Spring World! Test CICD";
	}

	public static void main(String[] args) {
		SpringApplication.run(AccounterApplication.class, args);
	}

}
