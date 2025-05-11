package com.kidou.comments_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidou.comments_api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNome(String nome);

    Optional<User> findByEmail(String email);

}
