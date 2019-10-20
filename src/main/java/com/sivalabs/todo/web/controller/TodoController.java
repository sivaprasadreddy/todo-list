package com.sivalabs.todo.web.controller;

import com.sivalabs.todo.config.security.SecurityUtils;
import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@PreAuthorize("hasRole('ROLE_USER')")
@Slf4j
public class TodoController {

    private final TodoService todoService;
    private final SecurityUtils securityUtils;

    public TodoController(TodoService todoService, SecurityUtils securityUtils) {
        this.todoService = todoService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody @Validated Todo todo) {
        todo.setCreatedBy(securityUtils.loginUser());
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.findById(id)
                .map(todoObj -> {
                    todo.setId(id);
                    todo.setCreatedBy(securityUtils.loginUser());
                    todo.setCreatedAt(todoObj.getCreatedAt());
                    todo.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(todoService.update(todo));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable Long id) {
        return todoService.findById(id)
                .map(todo -> {
                    todoService.deleteById(id);
                    return ResponseEntity.ok(todo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
