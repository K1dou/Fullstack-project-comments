package com.kidou.comments_api.model.dto;

public class AuthorDTO {
    private Long id;
    private String nome;
    private String avatarUrl;

    public AuthorDTO() {
    }

    public AuthorDTO(Long id, String nome, String avatarUrl) {
        this.id = id;
        this.nome = nome;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
