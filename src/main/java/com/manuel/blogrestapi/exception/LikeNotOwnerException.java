package com.manuel.blogrestapi.exception;

public class LikeNotOwnerException extends RuntimeException {

    private static final String MESSAGE = "It was not you who liked this post";

    public LikeNotOwnerException() {
        super(MESSAGE);
    }
}
