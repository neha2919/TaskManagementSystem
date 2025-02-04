package com.neha.TaskManagement.BaseStructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private String timestamp;
    private String message;
    private boolean status;
}
