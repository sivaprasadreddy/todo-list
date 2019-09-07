package com.sivalabs.todo.web.controller;

import com.sivalabs.todo.common.AbstractIntegrationTest;
import com.sivalabs.todo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class TodoControllerIT extends AbstractIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getCategories() {
        ResponseEntity<Todo[]> response = restTemplate.getForEntity("/api/todos", Todo[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
