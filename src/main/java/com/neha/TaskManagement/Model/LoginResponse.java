package com.neha.TaskManagement.Model;

import com.neha.TaskManagement.Dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserDto userDto;
}
