package com.kidou.comments_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidou.comments_api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

}
