package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Repository.TaskRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Task createTask(Task task, String email) {
        userRepository.findByEmailAndIsAdmin(email, true)
                .orElseThrow(() -> new RuntimeException("User is not an admin or not found"));
        return taskRepository.save(task);
    }

    @Override
    public Task getTaskById(UUID taskId, String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new RuntimeException("Only admin can view tasks"));
        return taskRepository.findById(taskId).orElseThrow(()->new RuntimeException("Task not found"));
    }

    @Override
    public List<Task> getAllTasks(String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new RuntimeException("Only admin can view all tasks"));
        return taskRepository.findAll();
    }

    @Override
    public Task updateTask(Task task, String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new RuntimeException("Only admin can update task"));
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public Task deleteTask(UUID taskId, String email) {
        userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(() -> new RuntimeException("Only admin can delete tasks"));
        Task taskToBeDeleted = taskRepository.findById(taskId).orElseThrow(()->new RuntimeException("Task id does not exist"));
        taskRepository.delete(taskToBeDeleted);
        return taskToBeDeleted;
    }
}
