package com.example.bdhv_itclub.repository;

import com.example.bdhv_itclub.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(String roleUser);

    boolean existsByName(String roleUser);
}