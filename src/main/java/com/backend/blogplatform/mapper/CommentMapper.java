package com.backend.blogplatform.mapper;

import com.backend.blogplatform.dto.request.CommentRequest;
import com.backend.blogplatform.dto.response.AuthorResponse;
import com.backend.blogplatform.dto.response.CommentResponse;
import com.backend.blogplatform.entity.Comment;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public CommentResponse toResponse(Comment comment){

        AuthorResponse author = userMapper.toAuthorResponse(comment.getAuthor());

        Long parentId = comment.getParentComment() != null
                ? comment.getParentComment().getId()
                : null;

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                author,
                parentId
        );
    }

    public Comment toEntity(CommentRequest request, User author, Post post, Comment parentComment){

        return new Comment(
                request.getContent(),
                post,
                author,
                parentComment
        );
    }
}
