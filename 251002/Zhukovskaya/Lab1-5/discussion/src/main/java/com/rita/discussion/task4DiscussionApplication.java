package com.rita.discussion;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
		info = @Info(title = "discussion", version = "v1")
)
@SpringBootApplication
public class task4DiscussionApplication {

	public static void main(String[] args) {

		SpringApplication.run(task4DiscussionApplication.class, args);
	}

}
