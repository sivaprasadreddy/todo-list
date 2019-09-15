package com.sivalabs.todo.config;

import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.sivalabs.todo.utils.Constants.PROFILE_NOT_HEROKU;
import static com.sivalabs.todo.utils.Constants.PROFILE_NOT_PROD;

@Slf4j
@Component
@Profile({PROFILE_NOT_PROD, PROFILE_NOT_HEROKU})
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public void run(String... args) {
        todoRepository.deleteAll();

        todoRepository.save(new Todo(null, "Learn SpringBoot", LocalDateTime.now(), true));
        todoRepository.save(new Todo(null, "Learn ReactJS", LocalDateTime.now(), false));
        todoRepository.save(new Todo(null, "Learn AWS", LocalDateTime.now(), false));

        log.debug("Data initialized");
    }
}
