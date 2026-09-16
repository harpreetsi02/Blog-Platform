package com.backend.blogplatform.repository;

import com.backend.blogplatform.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
