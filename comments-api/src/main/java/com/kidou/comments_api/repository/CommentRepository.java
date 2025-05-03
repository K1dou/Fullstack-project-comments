package com.kidou.comments_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidou.comments_api.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
