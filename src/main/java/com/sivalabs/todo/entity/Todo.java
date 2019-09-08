package com.sivalabs.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @SequenceGenerator(name = "todo_id_generator", sequenceName = "todo_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "todo_id_generator")
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Todo text cannot be empty")
    private String text;

    private LocalDateTime createdOn = LocalDateTime.now();

    private boolean done;

    @PrePersist
    @PreUpdate
    void prePersist() {
        if(createdOn == null) {
            createdOn = LocalDateTime.now();
        }
    }
}
