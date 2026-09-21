package com.backend.blogplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BlogplatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogplatformApplication.class, args);
	}

}
