package com.sivalabs.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor()
public class Todo implements Serializable {

    @Id
    @SequenceGenerator(name = "todo_id_generator", sequenceName = "todo_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "todo_id_generator")
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Todo text cannot be empty")
    private String text;

    private boolean done;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @JsonProperty("created_at")
    @Column(insertable = true, updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @JsonProperty("updated_at")
    @Column(insertable = false, updatable = true)
    protected LocalDateTime updatedAt = LocalDateTime.now();

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
