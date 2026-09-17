package com.backend.blogplatform.controller;

import com.backend.blogplatform.dto.request.CommentRequest;
import com.backend.blogplatform.dto.response.CommentResponse;
import com.backend.blogplatform.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse response =
                commentService.createComment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(
            @PathVariable Long postId
    ){
        List<CommentResponse> response =
                commentService.getCommentsByPost(postId);

        return ResponseEntity.ok(response);
    }
}
