package com.backend.blogplatform.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class PostRequest {

    @NotBlank(message = "Title is required!")
    private String title;

    @NotBlank(message = "Content is required!")
    private String content;

    private Set<String> tagNames;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<String> tagNames) {
        this.tagNames = tagNames;
    }
}
