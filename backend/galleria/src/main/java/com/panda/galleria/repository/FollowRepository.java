package com.panda.galleria.repository;

import com.panda.galleria.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollowerId(Long id);
    List<Follow> findByFollowingId(Long id);
}
