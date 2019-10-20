package com.sivalabs.todo.config;

import com.sivalabs.todo.entity.Todo;
import com.sivalabs.todo.entity.User;
import com.sivalabs.todo.repository.TodoRepository;
import com.sivalabs.todo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.sivalabs.todo.utils.Constants.PROFILE_NOT_HEROKU;
import static com.sivalabs.todo.utils.Constants.PROFILE_NOT_PROD;

@Slf4j
@Component
@Profile({PROFILE_NOT_PROD, PROFILE_NOT_HEROKU})
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public void run(String... args) {
        todoRepository.deleteAll();

        User admin = userRepository.findByEmail("admin@gmail.com").orElse(null);

        todoRepository.save(new Todo(null, "Learn SpringBoot", true, admin, LocalDateTime.now(), null));
        todoRepository.save(new Todo(null, "Learn ReactJS", false, admin, LocalDateTime.now(), null));
        todoRepository.save(new Todo(null, "Learn AWS", false, admin, LocalDateTime.now(), null));

        log.debug("Data initialized");
    }
}
