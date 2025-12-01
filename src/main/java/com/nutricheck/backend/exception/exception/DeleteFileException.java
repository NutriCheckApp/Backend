package com.nutricheck.backend.exception.exception;

import lombok.Getter;

@Getter
public class DeleteFileException extends RuntimeException {

    public DeleteFileException(String filename, Throwable cause) {
        super("Could not delete file: " + filename, cause);
    }

    public DeleteFileException(String filename) {
        super("Could not delete file: " + filename);
    }
}