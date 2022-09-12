package com.manuel.blogrestapi.exception;

public class CommentLikeWasAlreadyGivenException extends RuntimeException {

    private static final String MESSAGE = "Error when trying to like comment with id %d, you probably already gave it a like!";

    public CommentLikeWasAlreadyGivenException(long commentId) {
        super(MESSAGE.formatted(commentId));
    }
}
