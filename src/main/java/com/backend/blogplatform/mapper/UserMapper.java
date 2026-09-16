package com.backend.blogplatform.mapper;

import com.backend.blogplatform.dto.request.RegisterRequest;
import com.backend.blogplatform.dto.response.AuthorResponse;
import com.backend.blogplatform.dto.response.UserResponse;
import com.backend.blogplatform.entity.Role;
import com.backend.blogplatform.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user){

        Set<String> roleNames = user.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames
        );
    }

    public User toUserEntity(RegisterRequest request, String hashedPassword){

        return new User(
                request.getUsername(),
                request.getEmail(),
                hashedPassword,
                Set.of(Role.USER)
        );
    }

    public AuthorResponse toAuthorResponse(User user){

        return new AuthorResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
