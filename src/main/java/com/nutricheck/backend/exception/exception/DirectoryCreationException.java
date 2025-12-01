package com.nutricheck.backend.exception.exception;

import lombok.Getter;

@Getter
public class DirectoryCreationException extends RuntimeException {

    public DirectoryCreationException(Throwable cause) {
        super("Could not create the directory where the uploaded files will be stored.", cause);
    }
}