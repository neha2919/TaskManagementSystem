package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.UserRoleDto;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {
    public UserRoleDto createRole(UserRoleDto userRoleDto);
    public List<UserRoleDto> getAllRoles();
    public UserRoleDto getRoleById(UUID roleId);
    public UserRoleDto updateRole(UserRoleDto userRoleDto);
    public UserRoleDto delete(String roleName);
}
