package com.neha.TaskManagement.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private UUID id;
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be null")
    private String password;
    @NotNull(message = "Please fill all the details.")
    @NotBlank(message = "Please fill all the details.")
    private String firstName, lastName, fullName;
    @Email(message = "Please enter valid email")
    private String email;
    private Long phoneNumber;
    private Boolean isAdmin;
    //add task dto list too.
    private List<TaskDto> tasks;
    public String getFullName(){
        return firstName+" "+lastName;
    }

    public static User dtoToEntity(UserDto dto){
        User entity = new User();
        entity.setPassword(dto.getPassword());
        entity.setFullName(dto.getFullName());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
//        entity.setIsAdmin(dto.isAdmin);
        entity.setPhoneNumber(dto.getPhoneNumber());

        //Each TaskDto in the DTO must be converted into a Task entity before being assigned to User. This requires custom logic, which is implemented either using streams or a loop.
        //set taskDto list too.
        //review and understand properly
        if(dto.getTasks() != null){
            List<Task> tasksEntities = new ArrayList<>();
            for(TaskDto taskDto : dto.getTasks()){
                tasksEntities.add(TaskDto.dtoToEntity(taskDto));
            }
            entity.setTasks(tasksEntities);
        }

        return entity;
    }

    public static UserDto entityToDto(User entity){
        UserDto dto = new UserDto();

        dto.setPassword(entity.getPassword());
        dto.setFullName(entity.getFullName());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPhoneNumber(entity.getPhoneNumber());
//        dto.setIsAdmin(entity.getIsAdmin());

//        if (dto.isAdmin()) {
//            entity.setIsAdmin(true);
//        } else {
//            entity.setIsAdmin(false);
//        }

//        If getIsAdmin() is null, it defaults to false.
//        If getIsAdmin() is not null, it sets the value of isAdmin accordingly.
//        dto.setAdmin(entity.getIsAdmin() != null && entity.getIsAdmin());

        //set taskDto list too.
        if(entity.getTasks() != null){
            List<TaskDto> taskEntities = new ArrayList<>();
            for(Task task : entity.getTasks()){
                taskEntities.add(TaskDto.entityToDto(task));
            }
            dto.setTasks(taskEntities);
        }

        return  dto;
    }
}
