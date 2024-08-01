package com.panda.galleria.repository;

import com.panda.galleria.model.Post;
import com.panda.galleria.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByUserId(Long id);
}
