package com.backend.blogplatform.service;

import com.backend.blogplatform.dto.request.PostRequest;
import com.backend.blogplatform.dto.response.PostResponse;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.User;
import com.backend.blogplatform.exception.UserNotFoundException;
import com.backend.blogplatform.mapper.PostMapper;
import com.backend.blogplatform.repository.PostRepository;
import com.backend.blogplatform.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostMapper postMapper
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    private User getCurrentUser(){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    public PostResponse createPost(PostRequest request){

        User author = getCurrentUser();
        Post post = postMapper.toEntity(request, author);
        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }
}
