package com.neha.TaskManagement.Repository;

import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
//    Optional<User> findByEmailAndIsAdmin(String email, Boolean isAdmin);
    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<List<User>> findByTasks_TaskId(UUID taskId);
    List<User> findByUsernameIn(Set<String> email);
}

//@Query(value = "select * from users where email = :email and Admin = :isAdmin", nativeQuery = true)
//Optional<User> findByEmailAndAdmin(@Param("email") String email, @Param("isAdmin") Boolean isAdmin);
