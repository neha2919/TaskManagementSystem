package com.neha.TaskManagement.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neha.TaskManagement.Entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleDto {
    private UUID roleId;
    private String roleName;
    private String roleCode;
    private String description;

    public static UserRole dtoToEntity(UserRoleDto dto){
        UserRole entity = new UserRole();
        entity.setRoleId(dto.getRoleId());
        entity.setRoleCode(dto.getRoleCode());
        entity.setRoleName(dto.getRoleName());
        entity.setDescription(dto.getDescription());

        return entity;
    }
    public static UserRoleDto entityToDto(UserRole entity){
        UserRoleDto dto = new UserRoleDto();
        dto.setRoleId(entity.getRoleId());
        dto.setRoleCode(entity.getRoleCode());
        dto.setRoleName(entity.getRoleName());
        dto.setDescription(entity.getDescription());

        return dto;
    }
}