package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.dto.CommentDto;
import com.manuel.blogrestapi.entities.Comment;
import com.manuel.blogrestapi.exception.BlogAPIException;
import com.manuel.blogrestapi.exception.ResourceNotFoundException;
import com.manuel.blogrestapi.repository.CommentRepository;
import com.manuel.blogrestapi.dto.LikeDTO;
import com.manuel.blogrestapi.entities.Post;
import com.manuel.blogrestapi.repository.PostRepository;
import com.manuel.blogrestapi.entities.User;
import com.manuel.blogrestapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private ModelMapper modelMapper;

    private PostService postService;

    private UserService userService;

    private LikeService likeService;


    @Override
    public CommentDto createComment(String token, long postId, CommentDto commentDto) {

        Comment comment = commentRepository.save(
                buildComment(
                        token,
                        commentDto.getName(),
                        commentDto.getEmail(),
                        commentDto.getBody(),
                        postId
                )
        );

        return mapToDTO(comment);
    }

    private Comment buildComment(String token, String name, String email, String body, long postId) {
        return Comment.builder()
                .name(name)
                .email(email)
                .body(body)
                .author(getUser(token))
                .post(getPost(postId))
                .build();
    }

    private Post getPost(long id) {
        return postService.getPost(id);
    }

    private User getUser(String token){
        return userService.currentLoggedUser(token);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {

        List<Comment> commentsByPostId = commentRepository.findByPostId(postId);

        return commentsByPostId.stream()
                .map(comment -> mapToDTO(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    @Override
    public Comment getComment(long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }

    @Override
    public CommentDto updateCommentById(CommentDto commentDto, long postId, long commentId) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setPost(post);

        commentRepository.save(comment);

        return mapToDTO(comment);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {

        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }


        commentRepository.delete(comment);
    }


    @Override
    public LikeDTO giveLikeByCommentId(String token, long commentId) {
        return likeService.giveForCommentById(token.replace(AppConstants.HEADER_VALUE, ""), commentId);
    }

    private CommentDto mapToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}