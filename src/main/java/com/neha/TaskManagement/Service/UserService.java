package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User signupUser(UserDto userDto);
//    User loginUser(LoginRequest request);
    //User isAdmin(Long id);

    User loginUser(LoginRequestDto requestdto);

    UserDto isAdmin(String email);

    public void logoutUser();
    List<UserDto> getAllUsers();
    User getUserByEmail(String email);
    User deleteUser(UUID id);
    User isActive(User user);
    UserDto updateUser(UUID userId, UserDto userDto);
    List<UserDto> getUsersByTask(UUID taskId);
}
