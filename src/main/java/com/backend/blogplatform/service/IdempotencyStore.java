package com.backend.blogplatform.service;

import com.backend.blogplatform.dto.response.CommentResponse;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class IdempotencyStore {

    private final ConcurrentHashMap<String, CommentResponse> store = new ConcurrentHashMap<>();

    public CommentResponse get(String key){
        return store.get(key);
    }

    public void save(String key, CommentResponse response){
        store.put(key, response);
    }
}
