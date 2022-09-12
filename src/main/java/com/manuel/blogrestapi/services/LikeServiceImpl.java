package com.manuel.blogrestapi.services;


import com.manuel.blogrestapi.entities.Comment;
import com.manuel.blogrestapi.exception.CommentLikeWasAlreadyGivenException;
import com.manuel.blogrestapi.exception.LikeNotOwnerException;
import com.manuel.blogrestapi.exception.PostLikeWasAlreadyGivenException;
import com.manuel.blogrestapi.exception.PostNotExistsException;
import com.manuel.blogrestapi.dto.LikeDTO;
import com.manuel.blogrestapi.entities.Like;
import com.manuel.blogrestapi.models.LikeMapper;
import com.manuel.blogrestapi.entities.Post;
import com.manuel.blogrestapi.repository.LikeRepository;
import com.manuel.blogrestapi.entities.User;
import com.manuel.blogrestapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Lazy))
public class LikeServiceImpl implements LikeService {


    private final LikeRepository likeRepository;

    private final UserService userService;

    private final PostService postService;

    private final CommentService commentService;


    @Override
    public LikeDTO giveForPostById(String token, long post_id) {
        User user = getUser(token);
        Post post = getPost(post_id);

        if (isLikeInPost(user, post)) {
            throw new PostLikeWasAlreadyGivenException(post_id);
        }

        Like like = likeRepository.save(buildLikeForPost(user, post));

        return LikeMapper.buildForPost(like);
    }

    @Override
    public LikeDTO removeFromPostById(String token, long postId) {
        Like likeFromPost = getLikeFromPost(token, postId);
        likeRepository.delete(likeFromPost);
        return LikeMapper.buildForPost(likeFromPost);
    }

    @Override
    public LikeDTO giveForCommentById(String token, long commentId) {
        User user = getUser(token);
        Comment comment = getComment(commentId);

        if (isLikeInComment(user, comment)) {
            throw new CommentLikeWasAlreadyGivenException(commentId);
        }

        Like like = likeRepository.save(buildLikeForComment(user, comment));

        return LikeMapper.buildForComment(like);
    }

    private Like getLikeFromPost(String token, long id) {
        return getPost(id).getMyLikes().stream()
                .filter(like -> isOwnerOfLike(like, getUser(token.replace(AppConstants.HEADER_VALUE, ""))))
                .findFirst()
                .orElseThrow(() -> new PostNotExistsException(id));
    }

    private Like buildLikeForPost(User user, Post post) {
        return Like.builder()
                .user(user)
                .post(post)
                .build();
    }

    private Like buildLikeForComment(User user, Comment comment) {
        return Like.builder()
                .user(user)
                .comment(comment)
                .build();
    }

    private boolean isLikeInPost(User user, Post post) {
        return post.getMyLikes().stream()
                .anyMatch(like -> isOwnerOfLike(like, user));
    }

    private boolean isLikeInComment(User user, Comment comment) {
        return comment.getMyLikes().stream()
                .anyMatch(like -> isOwnerOfLike(like, user));
    }

    private boolean isOwnerOfLike(Like like, User user) {
        if (Objects.equals(like.getUser(), user)) {
            return true;
        } else {
            throw new LikeNotOwnerException();
        }
    }

    private User getUser(String token) {
        return userService.currentLoggedUser(token);
    }

    private Post getPost(long id) {
        return postService.getPost(id);
    }

    private Comment getComment(long id) {
        return commentService.getComment(id);
    }

}