package com.kidou.comments_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Like() {
    }

    public Like(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
