package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.service.RoleService;
import com.example.bdhv_itclub.utils.annotation.APIResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Ok
    @GetMapping("/get-all")
    @APIResponseMessage("Liệt kê tất cả vai trò")
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}