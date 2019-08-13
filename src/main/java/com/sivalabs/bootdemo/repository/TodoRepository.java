package com.sivalabs.bootdemo.repository;

import com.sivalabs.bootdemo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long> {
}
