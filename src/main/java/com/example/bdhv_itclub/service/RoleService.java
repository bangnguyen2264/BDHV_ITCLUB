package com.example.bdhv_itclub.service;

import com.example.bdhv_itclub.dto.reponse.RoleResponse;
import com.example.bdhv_itclub.dto.request.RoleFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<RoleResponse> getAll(RoleFilterRequest filter, Pageable pageable);
    RoleResponse getById(Integer id);
    RoleResponse addRole(String name);
    RoleResponse updateRole(Integer id,String name);
    void deleteRole(Integer id);
}
