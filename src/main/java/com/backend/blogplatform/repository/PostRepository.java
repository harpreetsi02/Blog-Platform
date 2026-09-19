package com.backend.blogplatform.repository;

import com.backend.blogplatform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

//    Page<Post> findByDeletedAtIsNull(Pageable pageable);

    Optional<Post> findByIdAndDeletedAtIsNull(Long postId);

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.deletedAt IS NULL")
    Page<Post> findAllWithAuthor(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL AND" +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))"
    )
    Page<Post> searchPosts(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.deleted_at IS NULL " +
            "AND MATCH(p.title, p.content) AGAINST(:keyword IN NATURAL LANGUAGE MODE)",
            nativeQuery = true
    )
    Page<Post> fullTextSearch(@Param("keyword") String keyword, Pageable pageable);
}