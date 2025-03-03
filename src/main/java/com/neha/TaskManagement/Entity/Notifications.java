package com.neha.TaskManagement.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue
    private UUID notificationsTd;
    private String description;
    private LocalDateTime timestamp;

    @ManyToOne
    //JoinColumn
    private User recipient;
    private boolean seen = false;

}
