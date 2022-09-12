package com.manuel.blogrestapi.repository;

import com.manuel.blogrestapi.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
