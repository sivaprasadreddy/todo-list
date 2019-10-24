package com.sivalabs.todo;

import com.sivalabs.todo.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.zalando.problem.spring.web.autoconfigure.security.SecurityConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class, SecurityConfiguration.class})
@EnableConfigurationProperties({ApplicationProperties.class})
public class TodoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

}
