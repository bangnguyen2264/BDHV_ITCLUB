package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Ok
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}