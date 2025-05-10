package com.rita.publisher;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@OpenAPIDefinition(
		info = @Info(title = "publisher", version = "v1")
)
@SpringBootApplication
@EnableCaching
public class publisherApplication {

	public static void main(String[] args) {

		SpringApplication.run(publisherApplication.class, args);
	}

}
