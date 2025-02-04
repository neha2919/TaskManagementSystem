package com.neha.TaskManagement.BaseStructure;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseApiStructure{
    public <T> ResponseEntity<ApiResponse<T>> sendSuccessfulApiResponse(T data, String message){
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(true);
        response.setMessage(message);
        response.setData(data);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public <T> ResponseEntity<ApiResponse<T>> sendFailedApiResponse(String message){
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(null);
        response.setStatus(false);
        response.setMessage(message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public <T> ResponseEntity<ApiResponse<T>> formApiResponse(T data){
        ApiResponse<T> response = new ApiResponse<>();
        response.setMessage("");
        response.setData(data);
        response.setStatus(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public ResponseEntity<ExceptionResponse> sendExceptionResponse(String message, HttpStatus status){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(message);
        exceptionResponse.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        exceptionResponse.setStatus(false);

        return new ResponseEntity<>(exceptionResponse,status);
    }
}
