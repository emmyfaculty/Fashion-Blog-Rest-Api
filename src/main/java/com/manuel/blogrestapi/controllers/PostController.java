package com.manuel.blogrestapi.controllers;

import com.manuel.blogrestapi.dto.LikeDTO;
import com.manuel.blogrestapi.dto.PostDto;
import com.manuel.blogrestapi.models.PostResponse;
import com.manuel.blogrestapi.services.PostService;
import com.manuel.blogrestapi.security.annotation.ForAdmin;
import com.manuel.blogrestapi.security.annotation.ForUser;
import com.manuel.blogrestapi.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "CRUD REST API for posts resources")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Create new post REST API")
    @ForAdmin
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,
                                              @RequestHeader(name = AppConstants.HEADER_NAME) String token) {
        return new ResponseEntity<>(postService.createPost(token, postDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "GET all posts REST API")
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "GET post by id REST API")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Update post by id REST API")
    @ForAdmin
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,
                                              @PathVariable(name = "id") long id) {

        return new ResponseEntity<>(postService.updatePost(postDto, id), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete post by id REST API")
    @ForAdmin
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
    }

    @ApiOperation("Adding a like to a post")
    @ForUser
    @PatchMapping("/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable(name = "id") long id,
                                            @RequestHeader(name = AppConstants.HEADER_NAME) String token) {
        postService.giveLikeByPostId(token, id);
        return new ResponseEntity<>("You liked this post.", HttpStatus.OK);
    }

    @ApiOperation("Deleting a like associated to a particular post")
    @ForUser
    @DeleteMapping("/{id}/like")
    public ResponseEntity<LikeDTO> unlikePost(@PathVariable(name = "id") long id,
                                              @RequestHeader(name = AppConstants.HEADER_NAME) String token) {
        return new ResponseEntity<>(postService.unlikePostById(token, id), HttpStatus.OK);
    }
}