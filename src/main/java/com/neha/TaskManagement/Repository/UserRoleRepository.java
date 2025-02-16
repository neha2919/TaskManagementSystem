package com.neha.TaskManagement.Repository;

import com.neha.TaskManagement.Entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    boolean existsByRoleName(String roleName);

    Optional<UserRole> findByRoleName(String roleName);
    Optional<UserRole> findByRoleCode(String roleCode);

}

