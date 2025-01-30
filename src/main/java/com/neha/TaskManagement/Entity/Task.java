package com.neha.TaskManagement.Entity;

import com.neha.TaskManagement.Model.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //GenerationType.IDENTITY - it works well with the auto increment feature
    private UUID taskId;
    private Long empId;
    private String title, description, status;
    private LocalDate dueDate;
    //Addition of task priority enum class.
    @Enumerated(EnumType.STRING)
    private Priority priority;
    //Many to Many tasks to users
    @ManyToMany(mappedBy = "tasks")
    private List<User> users;

    //create a static function entityToDto
    //create a static function dtoTOEntity
}
