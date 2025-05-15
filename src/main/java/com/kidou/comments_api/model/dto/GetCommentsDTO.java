package com.kidou.comments_api.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GetCommentsDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private AuthorDTO author;
    private List<GetCommentsDTO> replies = new ArrayList<>();
    private boolean likedByUser;


    public GetCommentsDTO() {
    }

    public GetCommentsDTO(Long id, String content, LocalDateTime createdAt, AuthorDTO author,
            List<GetCommentsDTO> replies, Integer likeCount, boolean likedByUser) {
        this.likedByUser= likedByUser;
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.replies = replies;
        this.likeCount = likeCount;
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

    public void setReplies(List<GetCommentsDTO> replies) {
        this.replies = replies;
    }

    public Long getId() {
        return id;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
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

    public List<GetCommentsDTO> getReplies() {
        return replies;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

}
