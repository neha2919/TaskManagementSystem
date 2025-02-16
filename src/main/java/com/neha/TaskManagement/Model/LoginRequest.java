package com.neha.TaskManagement.Model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "User ID cannot be empty")
    private String userId;
    @NotNull(message = "Password cannot be empty.")
    private String password;
}
