package com.sivalabs.todo.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Todo> todoList;

    @BeforeEach
    void setUp() {
        this.todoList = new ArrayList<>();
        this.todoList.add(new Todo(1L, "First Todo", LocalDateTime.now(), true));
        this.todoList.add(new Todo(2L, "Second Todo", LocalDateTime.now(), false));
        this.todoList.add(new Todo(3L, "Third Todo", LocalDateTime.now(), false));

        objectMapper.registerModule(new ProblemModule());
        objectMapper.registerModule(new ConstraintViolationProblemModule());
    }

    @Test
    void shouldFetchAllTodos() throws Exception {
        given(todoRepository.findAll()).willReturn(this.todoList);

        this.mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(todoList.size())));
    }

    @Test
    void shouldFindTodoById() throws Exception {
        Long todoId = 1L;
        Todo todo = new Todo(todoId, "New Todo", LocalDateTime.now(), false);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        this.mockMvc.perform(get("/api/todos/{id}", todoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())))
        ;
    }

    @Test
    void shouldReturn404WhenFetchingNonExistingTodo() throws Exception {
        Long todoId = 1L;
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        this.mockMvc.perform(get("/api/todos/{id}", todoId))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldCreateNewTodo() throws Exception {
        given(todoRepository.save(any(Todo.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
        Long todoId = 1L;
        Todo todo = new Todo(todoId, "Updated First Todo", LocalDateTime.now(), true);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(todoRepository.save(any(Todo.class))).willAnswer((invocation) -> invocation.getArgument(0));

        this.mockMvc.perform(put("/api/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Updated First Todo")))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(true)));

    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingTodo() throws Exception {
        Long todoId = 1L;
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());
        Todo todo = new Todo(todoId, "Updated First Todo", LocalDateTime.now(), true);

        this.mockMvc.perform(put("/api/todos/{id}", todoId)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteTodo() throws Exception {
        Long todoId = 1L;
        Todo todo = new Todo(todoId, "New Todo", LocalDateTime.now(), false);
        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        doNothing().when(todoRepository).deleteById(todo.getId());

        this.mockMvc.perform(
                delete("/api/todos/{id}", todo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(todo.getText())))
                .andExpect(jsonPath("$.createdOn", notNullValue()))
                .andExpect(jsonPath("$.done", is(todo.isDone())));

    }

    @Test
    void shouldReturn404WhenDeletingNonExistingTodo() throws Exception {
        Long todoId = 1L;
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        this.mockMvc.perform(delete("/api/todos/{id}", todoId))
                .andExpect(status().isNotFound());

    }

}
