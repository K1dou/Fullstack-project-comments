package com.kidou.comments_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCommentDTO {
    @NotBlank(message = "The field content is required")
    private String content;
    @NotNull(message = "The field userId is required")
    private Long userId;

    public CreateCommentDTO() {
    }

    public CreateCommentDTO(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
