package com.kidou.comments_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kidou.comments_api.enums.RoleName;
import com.kidou.comments_api.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

}
