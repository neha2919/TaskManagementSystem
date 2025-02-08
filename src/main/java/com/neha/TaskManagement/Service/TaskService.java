package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto createTask(Task task, String email);
    TaskDto getTaskById(UUID taskId, String email);
    List<TaskDto> getAllTasks(String email);
    Task updateTask(Task task, String email);
    Task deleteTask(UUID taskId, String email);
    TaskDto createSubtask(UUID parentTaskId, List<Task> subTasks);

}
