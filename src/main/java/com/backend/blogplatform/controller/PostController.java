package com.backend.blogplatform.controller;

import com.backend.blogplatform.dto.request.PostRequest;
import com.backend.blogplatform.dto.response.LikeResponse;
import com.backend.blogplatform.dto.response.PostResponse;
import com.backend.blogplatform.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse response =
                postService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable){

        Page<PostResponse> responses =
                postService.getAllPosts(pageable);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable Long id
    ) {

        PostResponse response =
                postService.getPostById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponse> toggleLike(
            @PathVariable Long postId
    ) {
        LikeResponse response =
                postService.toggleLike(postId);

        return ResponseEntity.ok(response);
    }
}
