package com.backend.blogplatform.repository;

import com.backend.blogplatform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByDeletedAtIsNull(Pageable pageable);

    Optional<Post> findByIdAndDeletedAtIsNull(Long postId);

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.deletedAt IS NULL")
    Page<Post> findAllWithAuthor(Pageable pageable);
}