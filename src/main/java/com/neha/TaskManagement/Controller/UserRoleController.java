package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.BaseStructure.ApiResponse;
import com.neha.TaskManagement.BaseStructure.BaseApiStructure;
import com.neha.TaskManagement.Dtos.UserRoleDto;
import com.neha.TaskManagement.Service.UserRoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/roles")
public class UserRoleController extends BaseApiStructure{
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserRoleDto>> createRole(@Valid @RequestBody UserRoleDto userRoleDto){
        return sendSuccessfulApiResponse(userRoleService.createRole(userRoleDto), "Role created successfully");
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserRoleDto>>> getAllRoles(){
        return sendSuccessfulApiResponse(userRoleService.getAllRoles(), "List of all roles");
    }
    @GetMapping("/id")
    public ResponseEntity<ApiResponse<UserRoleDto>> getRoleById(@PathVariable UUID roleId){
        return sendSuccessfulApiResponse(userRoleService.getRoleById(roleId), "Role does not exist");
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UserRoleDto>> updateRole(@Valid @RequestBody UserRoleDto userRoleDto){
        return sendSuccessfulApiResponse(userRoleService.createRole(userRoleDto), "Role Updated!");
    }

    @DeleteMapping("{roleName}")
    public ResponseEntity<ApiResponse<UserRoleDto>> delete(@PathVariable("roleName") String roleName){
        return sendSuccessfulApiResponse(userRoleService.delete(roleName),"Role deleted.");
    }
}
