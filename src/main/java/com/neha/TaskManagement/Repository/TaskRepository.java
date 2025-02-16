package com.neha.TaskManagement.Repository;

import com.neha.TaskManagement.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByTaskId(UUID taskId);
    List<Task> findByUsers_Id(UUID userId);

}
