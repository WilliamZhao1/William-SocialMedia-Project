package com.example.william.dao;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.william.entity.Post;

public interface PostDao extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
	Page<Post> findByAuthorId(int authorId, Pageable page);
}
