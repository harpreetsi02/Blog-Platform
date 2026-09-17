package com.backend.blogplatform.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private AuthorResponse author;
    private Set<String> tagNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponse(
            Long id, String title,
            String content, AuthorResponse author,
            Set<String> tagNames, LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.tagNames = tagNames;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public AuthorResponse getAuthor() {
        return author;
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
