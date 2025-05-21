package com.example.bdhv_itclub.service.impl;

import com.example.bdhv_itclub.dto.reponse.RoleResponse;
import com.example.bdhv_itclub.dto.request.RoleFilterRequest;
import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;


    @Override
    public Page<RoleResponse> getAll(RoleFilterRequest filter, Pageable pageable) {
        return roleRepository.findAll((root, query, cb) -> {
            var predicates = cb.conjunction();

            if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
                String keyword = "%" + filter.getQuery().toLowerCase() + "%";
                predicates = cb.and(predicates,
                        cb.like(cb.lower(root.get("name")), keyword));
            }

            return predicates;
        }, pageable).map(RoleResponse::from);
    }

    @Override
    public RoleResponse getById(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Role with id " + id + " not found"));
        return RoleResponse.from(role);
    }


    @Override
    public RoleResponse addRole(String name) {
        Role role = Role.builder()
                .name(name)
                .build();
        roleRepository.save(role);
        return RoleResponse.from(role);
    }

    @Override
    public RoleResponse updateRole(Integer id,String name) {
        Role role = roleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Role with id " + id + " not found"));
        role.setName(name);
        roleRepository.save(role);
        return RoleResponse.from(role);
    }

    @Override
    public void deleteRole(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Role with id " + id + " not found"));
        roleRepository.delete(role);
    }
}
