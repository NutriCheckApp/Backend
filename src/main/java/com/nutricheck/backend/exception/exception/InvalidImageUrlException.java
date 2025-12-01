package com.nutricheck.backend.exception.exception;

import lombok.Getter;

@Getter
public class InvalidImageUrlException extends RuntimeException {

    public InvalidImageUrlException() {
        super("Image url is null or empty");
    }

    public InvalidImageUrlException(String message) {
        super("InvalidImageUrlException: " + message);
    }

    public InvalidImageUrlException(String message, Throwable cause) {
        super("InvalidImageUrlException: " + message, cause);
    }
}