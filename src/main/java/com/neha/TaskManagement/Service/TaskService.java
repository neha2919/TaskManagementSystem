package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    Task createTask(Task task, String email);
    Task getTaskById(UUID taskId, String email);
    List<Task> getAllTasks(String email);
    Task updateTask(Task task, String email);
    Task deleteTask(UUID taskId, String email);

}
