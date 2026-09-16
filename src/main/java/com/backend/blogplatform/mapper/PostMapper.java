package com.backend.blogplatform.mapper;

import com.backend.blogplatform.dto.request.PostRequest;
import com.backend.blogplatform.dto.response.AuthorResponse;
import com.backend.blogplatform.dto.response.PostResponse;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public PostResponse toResponse(Post post){

        AuthorResponse author = userMapper.toAuthorResponse(post.getAuthor());

         return new PostResponse(
                 post.getId(),
                 post.getTitle(),
                 post.getContent(),
                 author,
                 post.getCreatedAt(),
                 post.getUpdatedAt()
         );
    }

    public Post toEntity(PostRequest request, User author){

        return new Post(
                request.getTitle(),
                request.getContent(),
                author
        );
    }
}
