package com.neha.TaskManagement.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Model.Priority;
import com.neha.TaskManagement.Model.Progress;
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
//    @NotNull(message = "Please mention the Employee Id")
    private Long empId;
    @NotNull(message = "Please mention the required details to proceed")
    private String title, description;
    @NotNull(message = "Please mention dua date")
    private LocalDate dueDate;
    @NotNull(message = "Please mention the priority (HIGH, MEDIUM OR LOW)")
    private Priority priority;

    //no need for UserDto, rather make it List<String> email. Please remove this and fix the logic accordingly.
    private List<UserDto> users;

    //newly added fields
    private List<String> username;
    private LocalDate assignedOn;
    private String assignedBy;
    private String createdBy;
    private Progress progress;
    private UUID parentTask;
    private List<UUID> subTasks;

    public static Task dtoToEntity(TaskDto dto){
        Task entity = new Task();
        entity.setTaskId(dto.getTaskId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setDueDate(dto.getDueDate());
        entity.setPriority(dto.getPriority());
        entity.setProgress(dto.getProgress());
        if(dto.getUsers() != null){
            List<User> userEntities = new ArrayList<>();
            for(UserDto userDto:dto.getUsers()){
                userEntities.add(UserDto.dtoToEntity(userDto));
            }
            entity.setUsers(userEntities);
        }
        //we just assign the email in the user object as list. Now when we save this object in the service layer we just populate all the
        //fields by findByEmail;
        if (dto.getUsername()!=null || !dto.getUsername().isEmpty()){
            entity.setUsers(dto.getUsername()
                    .stream()
                    .map(username -> {
                        User assignedUser =  new User();
                        assignedUser.setUsername(username);
                        return assignedUser;
                    }).toList());
        }
        entity.setAssignedOn(dto.getAssignedOn());
        entity.setAssignedBy(dto.getAssignedBy());
        entity.setCreatedBy(dto.getCreatedBy());
        //we just assign the taskId in the Task object. Now when we save this object in the service layer we just populate all the
        //fields by findById;
        if (dto.getParentTask()!=null){
            Task parentTask = new Task();
            parentTask.setTaskId(dto.getParentTask());

            entity.setParentTask(parentTask);
        }
        //we just assign the taskId in the Task object as list. Now when we save this object in the service layer we just populate all the
        //fields by findById;
        if (dto.getSubTasks()!=null){
            entity.setSubTasks(dto.getSubTasks()
                    .stream()
                    .map(subTaskId -> {
                        Task subTask = new Task();
                        subTask.setTaskId(subTaskId);

                        return subTask;
                    })
                    .toList());
        }
        return entity;
    }

    public static TaskDto entityToDto(Task entity){
        TaskDto dto = new TaskDto();

        dto.setTaskId(entity.getTaskId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDueDate(entity.getDueDate());
        dto.setPriority(entity.getPriority());
//        if(entity.getUsers() != null){
//            List<UserDto> userEntities = new ArrayList<>();
//            for(User user:entity.getUsers()){
//                userEntities.add(UserDto.entityToDto(user));
//            }
//            dto.setUsers(userEntities);
//        }
        dto.setAssignedOn(entity.getAssignedOn());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setAssignedBy(entity.getAssignedBy());
        dto.setProgress(entity.getProgress());
        dto.setParentTask(entity.getParentTask()!=null ? entity.getParentTask().getTaskId() : null);
        dto.setSubTasks(entity.getSubTasks()!=null ? entity.getSubTasks()
                .stream()
                .map(task -> {
                    return task.getTaskId();
                })
                .toList():null);
        dto.setUsername(entity.getUsers()!=null ? entity.getUsers()
                .stream()
                .map(user -> {
                    return user.getUsername();
                })
                .toList():null);
        return dto;
    }
}
