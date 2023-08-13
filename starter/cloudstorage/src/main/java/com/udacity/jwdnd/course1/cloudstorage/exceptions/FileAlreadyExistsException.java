package com.udacity.jwdnd.course1.cloudstorage.exceptions;

public class FileAlreadyExistsException extends RuntimeException {

    public FileAlreadyExistsException(String message) {
        super(message);
    }

}
