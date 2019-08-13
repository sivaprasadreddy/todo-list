package com.sivalabs.bootdemo;

import com.sivalabs.bootdemo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class BootDemoApplicationTests extends AbstractIntegrationTest {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	public void getCategories() {
		ResponseEntity<Todo[]> response = restTemplate.getForEntity("/api/todos", Todo[].class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
