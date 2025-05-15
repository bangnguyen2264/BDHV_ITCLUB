package com.example.bdhv_itclub.repositorry;

import com.example.bdhv_itclub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String roleUser);

    boolean existsByName(String roleUser);
}