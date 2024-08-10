package com.panda.galleria.repository;

import com.panda.galleria.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByUser_Username(String username);
    List<Like> findAllByPostId(Long id);
}
