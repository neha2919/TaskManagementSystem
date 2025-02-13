package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto signupUser(UserDto userDto);
//    User loginUser(LoginRequest request);
    //User isAdmin(Long id);
    UserDto loginUser(LoginRequestDto requestdto);

    UserDto assignRoleToUser(String email, String roleName);

    public void logoutUser();
    List<UserDto> getAllUsers();
    UserDto getUserByEmail(String email);
    User deleteUser(UUID id);
    User isActive(User user);
    UserDto updateUser(UserDto userDto);
    List<UserDto> getAssignedUsersByTask(UUID taskId);
}
