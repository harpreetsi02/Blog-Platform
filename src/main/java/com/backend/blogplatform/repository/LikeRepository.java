package com.backend.blogplatform.repository;

import com.backend.blogplatform.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUser_IdAndPost_Id(Long userId, Long postId);

    long countByPost_Id(Long postId);
}
