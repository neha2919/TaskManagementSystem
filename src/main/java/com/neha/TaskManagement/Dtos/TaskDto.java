package com.neha.TaskManagement.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Model.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {
    private UUID taskId;
    @NotNull(message = "Please mention the Employee Id")
    private Long empId;
    @NotNull(message = "Please mention the required details to proceed")
    private String title, description, status;
    @NotNull(message = "Please mention dua date")
    private LocalDate dueDate;
    @NotNull(message = "Please mention the priority (HIGH, MEDIUM OR LOW)")
    @Enumerated(EnumType.STRING)
    private Priority priority;
    private List<UserDto> users;

    public static Task dtoToEntity(TaskDto dto){
        Task entity = new Task();
        entity.setTaskId(dto.getTaskId());
        entity.setEmpId(dto.getEmpId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setDueDate(dto.getDueDate());
        entity.setPriority(dto.getPriority());
        if(dto.getUsers() != null){
            List<User> userEntities = new ArrayList<>();
            for(UserDto userDto:dto.getUsers()){
                userEntities.add(UserDto.dtoToEntity(userDto));
            }
            entity.setUsers(userEntities);
        }

        return entity;
    }

    public static TaskDto entityToDto(Task entity){
        TaskDto dto = new TaskDto();

        dto.setTaskId(entity.getTaskId());
        dto.setEmpId(entity.getEmpId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setDueDate(entity.getDueDate());
        dto.setPriority(entity.getPriority());
        if(entity.getUsers() != null){
            List<UserDto> userEntities = new ArrayList<>();
            for(User user:entity.getUsers()){
                userEntities.add(UserDto.entityToDto(user));
            }
            dto.setUsers(userEntities);
        }

        return dto;
    }
}
