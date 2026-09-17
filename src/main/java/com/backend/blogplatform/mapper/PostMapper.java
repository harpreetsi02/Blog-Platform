package com.backend.blogplatform.mapper;

import com.backend.blogplatform.dto.request.PostRequest;
import com.backend.blogplatform.dto.response.AuthorResponse;
import com.backend.blogplatform.dto.response.PostResponse;
import com.backend.blogplatform.entity.Post;
import com.backend.blogplatform.entity.Tag;
import com.backend.blogplatform.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public PostResponse toResponse(Post post){

        AuthorResponse author = userMapper.toAuthorResponse(post.getAuthor());

        Set<String> tagNames = post.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

         return new PostResponse(
                 post.getId(),
                 post.getTitle(),
                 post.getContent(),
                 author,
                 tagNames,
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
