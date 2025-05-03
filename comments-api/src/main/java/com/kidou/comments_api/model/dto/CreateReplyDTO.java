package com.kidou.comments_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateReplyDTO {

    @NotBlank(message = "The field content is required")
    private String content;
    @NotNull(message = "The field userId is required")
    private Long userId;
    @NotNull(message = "The field parentId is required")
    private Long parentId;

    public CreateReplyDTO() {
    }

    public CreateReplyDTO(String content, Long userId, Long parentId) {
        this.content = content;
        this.userId = userId;
        this.parentId = parentId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
