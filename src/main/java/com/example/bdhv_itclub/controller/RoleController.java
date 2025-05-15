package com.example.bdhv_itclub.controller;

import com.example.bdhv_itclub.dto.reponse.RoleResponse;
import com.example.bdhv_itclub.dto.request.RoleFilterRequest;
import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Lấy danh sách vai trò", description = "Trả về danh sách vai trò có hỗ trợ lọc theo tên, phân trang và sắp xếp.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
    })
    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getAllRoles(
            @ParameterObject RoleFilterRequest filter,
            @Parameter(description = "Số trang bắt đầu từ 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng phần tử mỗi trang") @RequestParam(defaultValue = "10") int entry
    ) {
        Sort sort = Sort.unsorted();
        if (filter.getField() != null && filter.getSort() != null) {
            Sort.Direction direction = Sort.Direction.valueOf(filter.getSort().name());
            sort = Sort.by(direction, filter.getField());
        }

        Pageable pageable = PageRequest.of(page, entry, sort);
        return ResponseEntity.ok(roleService.getAll(filter, pageable));
    }

    @Operation(summary = "Lấy vai trò theo ID", description = "Trả về thông tin chi tiết của một vai trò dựa trên ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy vai trò"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(
            @Parameter(description = "ID của vai trò cần lấy") @PathVariable Long id
    ) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @Operation(summary = "Thêm vai trò mới", description = "Tạo mới một vai trò bằng tên vai trò.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo vai trò thành công"),
            @ApiResponse(responseCode = "400", description = "Tên vai trò không hợp lệ", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(
            @Parameter(description = "Tên của vai trò mới") @RequestBody String name
    ) {
        return ResponseEntity.ok(roleService.addRole(name));
    }

    @Operation(summary = "Cập nhật tên vai trò", description = "Cập nhật tên của một vai trò dựa theo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(
            @Parameter(description = "ID của vai trò cần cập nhật") @PathVariable Long id,
            @Parameter(description = "Tên mới của vai trò") @RequestBody String name
    ) {
        return ResponseEntity.ok(roleService.updateRole(id, name));
    }

    @Operation(summary = "Xoá vai trò", description = "Xoá vai trò dựa trên ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Xoá thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy vai trò", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID của vai trò cần xoá") @PathVariable Long id
    ) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
