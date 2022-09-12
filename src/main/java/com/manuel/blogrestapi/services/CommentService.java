package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.dto.CommentDto;
import com.manuel.blogrestapi.dto.LikeDTO;
import com.manuel.blogrestapi.entities.Comment;

import java.util.List;

public interface CommentService {

    CommentDto createComment(String token, long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(long postId, long commentId);

    Comment getComment(long id);

    CommentDto updateCommentById(CommentDto commentDto, long postId, long commentId);

    void deleteCommentById(long postId, long commentId);

    LikeDTO giveLikeByCommentId(String token, long commentId);

}