package com.nutricheck.backend.exception.exception;

import lombok.Getter;

@Getter
public class ImageNotExistsException extends RuntimeException {

    public ImageNotExistsException() {
        super("Image url is null or empty");
    }
}