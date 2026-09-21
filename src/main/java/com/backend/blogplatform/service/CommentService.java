package com.backend.blogplatform.service;

import com.backend.blogplatform.dto.request.CommentRequest;
import com.backend.blogplatform.dto.response.CommentResponse;
import com.backend.blogplatform.entity.Comment;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.User;
import com.backend.blogplatform.exception.CommentNotFoundException;
import com.backend.blogplatform.exception.PostNotFoundException;
import com.backend.blogplatform.exception.UserNotFoundException;
import com.backend.blogplatform.mapper.CommentMapper;
import com.backend.blogplatform.repository.CommentRepository;
import com.backend.blogplatform.repository.PostRepository;
import com.backend.blogplatform.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final IdempotencyStore idempotencyStore;

    public CommentService(
            UserRepository userRepository, CommentRepository commentRepository,
            CommentMapper commentMapper, PostRepository postRepository,
            IdempotencyStore idempotencyStore
    ) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.idempotencyStore = idempotencyStore;
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

    public CommentResponse createComment(String idempotencyKey, CommentRequest request){

        if (idempotencyKey != null){
            CommentResponse existing = idempotencyStore.get(idempotencyKey);
            if (existing != null){
                return existing;
            }
        }

        User user = getCurrentUser();

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() ->
                        new PostNotFoundException(
                                "Post not found with id: " + request.getPostId()
                        )
                );

        Comment parentComment = null;
        if (request.getParentCommentId() != null){
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() ->
                            new CommentNotFoundException(
                                    "Parent comment not found with id: " + request.getParentCommentId()
                            )
                    );
        }

        Comment comment = commentMapper.toEntity(request, user, post, parentComment);
        Comment savedComment = commentRepository.save(comment);

        CommentResponse response = commentMapper.toResponse(savedComment);

        if (idempotencyKey != null){
            idempotencyStore.save(idempotencyKey, response);
        }

        return response;
    }

    public List<CommentResponse> getCommentsByPost(Long postId){

        List<Comment> comments = commentRepository.findByPost_Id(postId);

        return comments.stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
