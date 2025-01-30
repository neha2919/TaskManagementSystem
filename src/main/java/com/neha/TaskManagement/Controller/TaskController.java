package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Service.TaskService;
import com.neha.TaskManagement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @PostMapping("/create")
    public Task createTask(@RequestBody Task task, @RequestParam String email){
        return taskService.createTask(task, email);
    }

    @GetMapping("{taskId}")
    public Task getTaskById(@PathVariable UUID taskId, @RequestParam String email){
        return taskService.getTaskById(taskId, email);
    }

    @GetMapping("all")
    public List<Task> getAllTasks(@RequestParam String email){
        return taskService.getAllTasks(email);
    }

    @PutMapping("update")
    public Task updateTask(@RequestBody Task task, @RequestParam String email){
        return taskService.updateTask(task, email);
    }

    @DeleteMapping("{taskId}")
    public Task deleteTask(@PathVariable UUID taskId, String email){
        return taskService.deleteTask(taskId, email);
    }
}
