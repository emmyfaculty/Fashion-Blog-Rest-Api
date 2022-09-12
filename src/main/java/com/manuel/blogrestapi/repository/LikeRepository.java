package com.manuel.blogrestapi.repository;

import com.manuel.blogrestapi.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

}