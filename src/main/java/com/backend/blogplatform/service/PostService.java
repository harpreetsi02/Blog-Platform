package com.backend.blogplatform.service;

import com.backend.blogplatform.dto.request.PostRequest;
import com.backend.blogplatform.dto.response.LikeResponse;
import com.backend.blogplatform.dto.response.PostResponse;
import com.backend.blogplatform.entity.Like;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.Tag;
import com.backend.blogplatform.entity.User;
import com.backend.blogplatform.exception.PostNotFoundException;
import com.backend.blogplatform.exception.UnauthorizedActionException;
import com.backend.blogplatform.exception.UserNotFoundException;
import com.backend.blogplatform.mapper.PostMapper;
import com.backend.blogplatform.repository.LikeRepository;
import com.backend.blogplatform.repository.PostRepository;
import com.backend.blogplatform.repository.TagRepository;
import com.backend.blogplatform.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostMapper postMapper,
            TagRepository tagRepository,
            LikeRepository likeRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
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

        Page<Post> posts = postRepository.findAllWithAuthor(pageable);

        return posts.map(postMapper::toResponse);
    }

    @Cacheable(value = "posts", key = "#id")
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id){

        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() ->
                        new PostNotFoundException(
                                "Post not found with id: " + id
                        )
                );

        return postMapper.toResponse(post);
    }

    @Transactional
    public LikeResponse toggleLike(Long postId){

        User user = getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new PostNotFoundException(
                                "Post not found with id: " + postId
                        )
                );

        Optional<Like> existingLike = likeRepository.findByUser_IdAndPost_Id(user.getId(), postId);

        boolean liked;

        if (existingLike.isPresent()){
            likeRepository.delete(existingLike.get());
            liked = false;
        } else {
            Like like = new Like(user, post);
            likeRepository.save(like);
            liked = true;
        }

        long likeCount = likeRepository.countByPost_Id(postId);

        return new LikeResponse(liked, likeCount);
    }

    @CacheEvict(value = "posts", key = "#postId")
    @Transactional
    public void deletePost(Long postId){

        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() ->
                        new PostNotFoundException(
                                "Post not found with postId: " + postId
                        )
                );

        if (!post.getAuthor().getId().equals(getCurrentUser().getId())){
            throw new UnauthorizedActionException(
                    "You are not able to delete this post."
            );
        }

        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> searchPosts(String keyword, Pageable pageable){

        Page<Post> posts = postRepository.searchPosts(keyword, pageable);

        return posts.map(postMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> fullTextSearch(String keyword, Pageable pageable){

        Page<Post> posts = postRepository.fullTextSearch(keyword, pageable);

        return posts.map(postMapper::toResponse);
    }

    @CacheEvict(value = "posts", key = "#postId")
    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request){

        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() ->
                        new PostNotFoundException(
                                "Post not found with post ID: " + postId
                        )
                );

        if (!post.getAuthor().getId().equals(getCurrentUser().getId())){
            throw new UnauthorizedActionException(
                    "You are not able to update this post."
            );
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        Set<Tag> tags = resolveTags(request.getTagNames());
        post.setTags(tags);

        postRepository.save(post);
        return postMapper.toResponse(post);
    }
}
