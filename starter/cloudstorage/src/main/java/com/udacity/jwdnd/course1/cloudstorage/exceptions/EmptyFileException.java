package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class EmptyFileException extends RuntimeException {

    public EmptyFileException(String message) {
        super(message);
    }

    public EmptyFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
