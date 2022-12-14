package com.manuel.blogrestapi.services;

import com.manuel.blogrestapi.dto.LikeDTO;
import com.manuel.blogrestapi.dto.PostDto;
import com.manuel.blogrestapi.models.PostResponse;
import com.manuel.blogrestapi.exception.ResourceNotFoundException;
import com.manuel.blogrestapi.entities.Post;
import com.manuel.blogrestapi.repository.PostRepository;
import com.manuel.blogrestapi.entities.User;
import com.manuel.blogrestapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final LikeService likeService;


    @Override
    public PostDto createPost(String token, PostDto postDto) {

        Post newPost = postRepository.save(
                buildPost(
                        token.replace(AppConstants.HEADER_VALUE, ""),
                        postDto.getContent(),
                        postDto.getTitle(),
                        postDto.getDescription()
                )
        );

        return mapToDTO(newPost);
    }

    private Post buildPost(String token, String content, String title, String description) {

        return Post.builder()
                .title(title)
                .description(description)
                .content(content)
                .author(getUser(token))
                .build();
    }

    private User getUser(String token){
        return userService.currentLoggedUser(token);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public Post getPost(long id) {
        return postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        postRepository.delete(post);
    }

    private PostDto mapToDTO(Post post) {

        return modelMapper.map(post, PostDto.class);
    }

    @Override
    public LikeDTO giveLikeByPostId(String token, long postId) {
        return likeService.giveForPostById(token.replace(AppConstants.HEADER_VALUE, ""), postId);
    }

    @Override
    public LikeDTO unlikePostById(String token, long id) {
        return likeService.removeFromPostById(token, id);
    }
}
