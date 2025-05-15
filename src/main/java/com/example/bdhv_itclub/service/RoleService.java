package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.RoleResponse;
import com.example.bdhv_itclub.dto.request.RoleFilterRequest;
import com.example.bdhv_itclub.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    Page<RoleResponse> getAll(RoleFilterRequest filter, Pageable pageable);
    RoleResponse getById(Long id);
    RoleResponse addRole(String name);
    RoleResponse updateRole(Long id,String name);
    void deleteRole(Long id);
}
