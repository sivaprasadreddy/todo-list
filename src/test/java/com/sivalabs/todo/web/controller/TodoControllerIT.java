package com.sivalabs.todo.web.controller;

import com.sivalabs.todo.common.AbstractIntegrationTest;
import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    private List<Todo> todoList = null;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();

        todoList = new ArrayList<>();
        todoList.add(new Todo(1L, "First Todo", LocalDateTime.now(), true));
        todoList.add(new Todo(2L, "Second Todo", LocalDateTime.now(), false));
        todoList.add(new Todo(3L, "Third Todo", LocalDateTime.now(), false));
        todoList = todoRepository.saveAll(todoList);
    }

    @Test
    void shouldFetchAllTodos() throws Exception {
        this.mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(todoList.size())));
    }

    @Test
    void shouldFindTodoById() throws Exception {
        Todo todo = todoList.get(0);
        Long todoId = todo.getId();

        this.mockMvc.perform(get("/api/todos/{id}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())))
        ;
    }

    @Test
    void shouldCreateNewTodo() throws Exception {
        Todo todo = new Todo(null, "New Todo", LocalDateTime.now(), false);
        this.mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

    @Test
    void shouldReturn400WhenCreateNewTodoWithoutText() throws Exception {
        Todo todo = new Todo(null, null, LocalDateTime.now(), false);

        this.mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", is("application/problem+json")))
                .andExpect(jsonPath("$.type", is("https://zalando.github.io/problem/constraint-violation")))
                .andExpect(jsonPath("$.title", is("Constraint Violation")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field", is("text")))
                .andExpect(jsonPath("$.violations[0].message", is("Todo text cannot be empty")))
                .andReturn()
        ;
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        Todo todo = todoList.get(0);
        todo.setText("Updated Todo");
        todo.setDone(true);

        this.mockMvc.perform(put("/api/todos/{id}", todo.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

    @Test
    void shouldDeleteTodo() throws Exception {
        Todo todo = todoList.get(0);

        this.mockMvc.perform(
                delete("/api/todos/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

}
