package com.sivalabs.bootdemo.config;

import com.sivalabs.bootdemo.entity.Todo;
import com.sivalabs.bootdemo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Profile("!prod")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public void run(String... args) {
        todoRepository.save(new Todo(null, "Learn SpringBoot", LocalDateTime.now(), true));
        todoRepository.save(new Todo(null, "Learn ReactJS", LocalDateTime.now(), false));
        todoRepository.save(new Todo(null, "Learn AWS", LocalDateTime.now(), false));

        log.debug("Data initialized");
    }
}
