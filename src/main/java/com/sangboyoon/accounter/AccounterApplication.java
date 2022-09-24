package com.sangboyoon.accounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AccounterApplication {

	@GetMapping("/")
	public String hello() {
		return "Hello Spring World";
	}

	public static void main(String[] args) {
		SpringApplication.run(AccounterApplication.class, args);
	}

}
