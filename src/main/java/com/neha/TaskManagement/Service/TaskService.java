package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto, String email);
    TaskDto getTaskById(UUID taskId, String email);
    List<TaskDto> getAllTasks(String email);

    /**TaskDto updateTask(TaskDto task, String email);**/
    Task deleteTask(UUID taskId, String email);
    List<TaskDto> createSubtask(UUID parentTaskId, List<TaskDto> subTasks);

}
