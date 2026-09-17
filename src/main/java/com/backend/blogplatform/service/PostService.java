package com.backend.blogplatform.service;

import com.backend.blogplatform.dto.request.PostRequest;
import com.backend.blogplatform.dto.response.PostResponse;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.Tag;
import com.backend.blogplatform.entity.User;
import com.backend.blogplatform.exception.PostNotFoundException;
import com.backend.blogplatform.exception.UserNotFoundException;
import com.backend.blogplatform.mapper.PostMapper;
import com.backend.blogplatform.repository.PostRepository;
import com.backend.blogplatform.repository.TagRepository;
import com.backend.blogplatform.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostMapper postMapper,
            TagRepository tagRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.tagRepository = tagRepository;
    }

    private Set<Tag> resolveTags(Set<String> tagNames){

        if (tagNames == null || tagNames.isEmpty()){
            return new HashSet<>();
        }

        Set<Tag> tags = new HashSet<>();

        for (String name : tagNames){
            Tag tag = tagRepository.findByName(name)
                    .orElseGet(() -> tagRepository.save(new Tag(name)));
            tags.add(tag);
        }

        return tags;
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

    @Transactional
    public PostResponse createPost(PostRequest request){

        User author = getCurrentUser();
        Post post = postMapper.toEntity(request, author);

        Set<Tag> tags = resolveTags(request.getTagNames());
        post.setTags(tags);

        Post savedPost = postRepository.save(post);

        return postMapper.toResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable){

        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(postMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new PostNotFoundException(
                                "Post not found with id: " + id
                        )
                );

        return postMapper.toResponse(post);
    }
}
