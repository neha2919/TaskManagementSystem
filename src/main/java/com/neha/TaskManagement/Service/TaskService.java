package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    TaskDto getTaskById(UUID taskId);
    List<TaskDto> getAllTasks();
    TaskDto updateTask(TaskDto task);
    Task deleteTask(UUID taskId);
    List<TaskDto> createSubtask(UUID parentTaskId, List<TaskDto> subTasks);
    List<TaskDto> getTaskByUser(String username);
    List<String> assignNewUsers(TaskDto taskDto);
    List<String> removeAssignedUser(TaskDto taskDto);
    List<TaskDto> getTaskByIds(List<UUID> uuidList);

}
