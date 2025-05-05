package com.kidou.comments_api.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kidou.comments_api.model.Comment;

public class GetCommentsDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private AuthorDTO author;
    private List<Comment> replies = new ArrayList<>();

    public GetCommentsDTO() {
    }

    public GetCommentsDTO(Long id, String content, LocalDateTime createdAt, AuthorDTO author, List<Comment> replies) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.replies = replies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public List<Comment> getReplies() {
        return replies;
    }

}
