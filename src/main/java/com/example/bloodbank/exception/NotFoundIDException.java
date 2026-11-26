package com.example.bloodbank.exception;

public class NotFoundIDException extends RuntimeException {
    public NotFoundIDException(String message) {
        super(message);
    }
}
