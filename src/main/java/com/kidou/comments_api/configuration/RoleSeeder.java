package com.kidou.comments_api.configuration;

import org.springframework.stereotype.Component;

import com.kidou.comments_api.enums.RoleName;
import com.kidou.comments_api.model.Role;
import com.kidou.comments_api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                return roleRepository.save(role);
            });
        }
    }

}
