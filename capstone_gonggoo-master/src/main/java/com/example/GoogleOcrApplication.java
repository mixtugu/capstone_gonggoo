package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"com.example.groupbuying.domain.repository",
		"com.example.chat.repository",
		"com.example.taxi.domain.repository",
		"com.example.community.domain.repository",
})

public class
GoogleOcrApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleOcrApplication.class, args);
	}

}
