package com.sivalabs.todo.web.controller;

import com.sivalabs.todo.common.AbstractIntegrationTest;
import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.entity.User;
import com.sivalabs.todo.repository.TodoRepository;
import com.sivalabs.todo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerIT extends AbstractIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private List<Todo> todoList = null;

    private User user;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();

        user = userRepository.findByEmail("admin@gmail.com").orElse(null);

        todoList = new ArrayList<>();
        this.todoList.add(new Todo(1L, "First Todo", true, user, LocalDateTime.now(), null));
        this.todoList.add(new Todo(2L, "Second Todo", true, user, LocalDateTime.now(), null));
        this.todoList.add(new Todo(3L, "Third Todo", true, user, LocalDateTime.now(), null));

        todoList = todoRepository.saveAll(todoList);
    }

    @Test
    @WithMockUser
    void shouldFetchAllTodos() throws Exception {
        this.mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(todoList.size())));
    }

    @Test
    @WithMockUser
    void shouldFindTodoById() throws Exception {
        Todo todo = todoList.get(0);
        Long todoId = todo.getId();

        this.mockMvc.perform(get("/api/todos/{id}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.created_at", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())))
        ;
    }

    @Test
    @WithMockUser(username = "admin@gmail.com")
    void shouldCreateNewTodo() throws Exception {
        Todo todo = new Todo(null, "New Todo", true, user, LocalDateTime.now(), null);
        this.mockMvc.perform(post("/api/todos")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.created_at", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

    @Test
    @WithMockUser
    void shouldReturn400WhenCreateNewTodoWithoutText() throws Exception {
        Todo todo = new Todo(null, null, true, user, LocalDateTime.now(), null);

        this.mockMvc.perform(post("/api/todos")
                .contentType(APPLICATION_JSON)
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
    @WithMockUser(username = "admin@gmail.com")
    void shouldUpdateTodo() throws Exception {
        Todo todo = todoList.get(0);
        todo.setText("Updated Todo");
        todo.setDone(true);

        this.mockMvc.perform(put("/api/todos/{id}", todo.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.created_at", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

    @Test
    @WithMockUser
    void shouldDeleteTodo() throws Exception {
        Todo todo = todoList.get(0);

        this.mockMvc.perform(
                delete("/api/todos/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.created_at", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

}
