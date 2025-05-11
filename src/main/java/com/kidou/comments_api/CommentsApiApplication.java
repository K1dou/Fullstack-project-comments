package com.kidou.comments_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommentsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentsApiApplication.class, args);

	}

}
