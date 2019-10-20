package com.sivalabs.todo.service;

import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.exception.TodoApplicationException;
import com.sivalabs.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo create(Todo todo){
        return todoRepository.saveAndFlush(todo);
    }

    public Todo update(Todo todo) {
        Optional<Todo> byId = todoRepository.findById(todo.getId());
        if(byId.isPresent()) {
           return todoRepository.save(todo);
        }
        throw new TodoApplicationException("Todo not found");
    }

    public void deleteById(Long id) {
        Optional<Todo> byId = todoRepository.findById(id);
        if(byId.isPresent()) {
            todoRepository.deleteById(id);
        }
    }
}
