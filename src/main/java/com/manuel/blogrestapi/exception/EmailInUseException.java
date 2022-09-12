package com.manuel.blogrestapi.exception;

public class EmailInUseException extends RuntimeException {

    private static final String MESSAGE = "Error while creating user, probably user with email %s exists in base!";

    public EmailInUseException(String value) {
        super(MESSAGE.formatted(value));
    }
}

