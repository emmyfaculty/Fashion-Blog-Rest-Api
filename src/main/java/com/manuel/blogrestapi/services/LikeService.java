package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.dto.LikeDTO;

public interface LikeService {

    LikeDTO giveForPostById(String token, long postId);

    LikeDTO removeFromPostById(String token, long postId);

    LikeDTO giveForCommentById(String token, long postId);

}