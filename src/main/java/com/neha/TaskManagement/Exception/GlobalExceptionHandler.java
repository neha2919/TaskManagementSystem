package com.neha.TaskManagement.Exception;

import com.neha.TaskManagement.BaseStructure.BaseApiStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@SuppressWarnings("rawtypes")
@ControllerAdvice
public class GlobalExceptionHandler extends BaseApiStructure {
    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception e){
        return sendExceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundExceptionHandler(NotFoundException nfe){
        return sendExceptionResponse(nfe.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NullException.class)
    public ResponseEntity nullExceptionHandler(NullException ne){
        return sendExceptionResponse(ne.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity processingExceptionHandler(ProcessingException pe){
        return sendExceptionResponse(pe.getMessage(), HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity unauthorizedExceptionHandler(UnauthorizedException ue){
        return sendExceptionResponse(ue.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity conflictExceptionHandler(ConflictException ce){
        return sendExceptionResponse(ce.getMessage(),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity notAllowedExceptionHandler(NotAllowedException nae){
        return sendExceptionResponse(nae.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }
}
