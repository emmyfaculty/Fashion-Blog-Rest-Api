package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.dto.LikeDTO;
import com.manuel.blogrestapi.dto.PostDto;
import com.manuel.blogrestapi.models.PostResponse;
import com.manuel.blogrestapi.entities.Post;


public interface PostService {
    PostDto createPost(String token, PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    Post getPost(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);

    LikeDTO giveLikeByPostId(String token, long postId);

    LikeDTO unlikePostById(String token, long id);

}