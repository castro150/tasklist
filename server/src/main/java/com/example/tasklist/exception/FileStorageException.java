package com.example.tasklist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageException extends RuntimeException {

    private static final long serialVersionUID = 4L;

    @Getter
    private String message;

    public FileStorageException(String message) {
        super(message);
        this.message = message;
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}