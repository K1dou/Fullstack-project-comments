package com.kidou.comments_api.model.dto;

public class UpdateCommentDTO {

    private String content;

    public UpdateCommentDTO() {
    }

    public UpdateCommentDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
