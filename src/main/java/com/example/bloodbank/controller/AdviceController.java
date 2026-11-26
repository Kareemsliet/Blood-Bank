package com.example.bloodbank.controller;

import com.example.bloodbank.dto.FieldValidationDto;
import com.example.bloodbank.dto.response.MainResponse;
import com.example.bloodbank.exception.AuthenticationException;
import com.example.bloodbank.exception.NotFoundIDException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MainResponse> handleConstraintViolations(ConstraintViolationException ex){
        List<FieldValidationDto> errors = ex.getConstraintViolations().stream().map(violation -> {
            String[] fieldSplit = violation.getPropertyPath().toString().split("\\.");
            FieldValidationDto fieldValidation = new FieldValidationDto();
            fieldValidation.setField(fieldSplit[2]);
            fieldValidation.setMessage(violation.getMessage());
            return  fieldValidation;
        }).toList();
        return ResponseEntity.badRequest().body(
               new MainResponse(
                       false,
                       errors.getFirst().getMessage(),
                       errors
               )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleRequestTypeMismatch(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<MainResponse> handleUserNotFound(AuthenticationException ex){
        return ResponseEntity.status(401).body(
                new MainResponse(false,ex.getMessage(),null)
        );
    }

    @ExceptionHandler(NotFoundIDException.class)
    public ResponseEntity<?> handleNotFoundId(){
        return ResponseEntity.notFound().build();
    }

}
