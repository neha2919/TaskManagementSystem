package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.UserRoleDto;
import com.neha.TaskManagement.Entity.UserRole;
import com.neha.TaskManagement.Exception.ConflictException;
import com.neha.TaskManagement.Exception.NotFoundException;
import com.neha.TaskManagement.Repository.UserRoleRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserRoleDto createRole(UserRoleDto userRoleDto) {
        if(userRoleRepository.existsByRoleName(userRoleDto.getRoleName())){
            throw new ConflictException("Role already exists!");
        }
        UserRole newUserRole = userRoleRepository.save(UserRoleDto.dtoToEntity(userRoleDto));
        return UserRoleDto.entityToDto(newUserRole);
    }

    @Override
    public List<UserRoleDto> getAllRoles() {
        List<UserRoleDto> userRoleDto = new ArrayList<>();
        for(UserRole userRole : userRoleRepository.findAll()){
            userRoleDto.add(UserRoleDto.entityToDto(userRole));
        }
        return userRoleDto;
    }

    @Override
    public UserRoleDto getRoleById(UUID roleId) {
        return UserRoleDto.entityToDto(userRoleRepository.findById(roleId)
                .orElseThrow(()->new NotFoundException("Role does not exists")));
    }

    @Override
    public UserRoleDto updateRole(UserRoleDto userRoleDto) {
        UserRole userRole = userRoleRepository.findByRoleName(userRoleDto.getRoleName())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        userRole.setRoleName(userRoleDto.getRoleName());
        userRole.setRoleCode(userRoleDto.getRoleCode());
        userRole.setDescription(userRoleDto.getDescription());

        UserRole updateUserRole = userRoleRepository.save(userRole);

        return UserRoleDto.entityToDto(updateUserRole);
    }

}
