package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.BaseStructure.ApiResponse;
import com.neha.TaskManagement.BaseStructure.BaseApiStructure;
import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Service.TaskService;
import com.neha.TaskManagement.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/tasks")
public class TaskController extends BaseApiStructure {
    @Autowired
    private TaskService taskService;
    //convert all task to TaskDto and apply @Valid where needed.
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@RequestBody TaskDto taskdto, @RequestParam String email){
        TaskDto newTask = taskService.createTask(taskdto, email);
        return sendSuccessfulApiResponse(newTask,"Task Initiated.");
    }

    @GetMapping("{taskId}")
    public ResponseEntity getTaskById(@PathVariable UUID taskId, @RequestParam String email){
        return sendSuccessfulApiResponse(taskService.getTaskById(taskId, email),"Task viewed.");
    }

    @GetMapping("all")
    public ResponseEntity getAllTasks(@RequestParam String email){
        return sendSuccessfulApiResponse(taskService.getAllTasks(email),"All assigned task for user.");
    }

    @PutMapping("update")
    public ResponseEntity updateTask(@RequestBody TaskDto taskDto, @RequestParam String email){
        return sendSuccessfulApiResponse(taskService.updateTask(taskDto, email),"Task updated.");
    }
    @PostMapping("{parentTask}/create-subtask")
    public ResponseEntity createSubtask(@RequestParam String email,@PathVariable("parentTaskId")UUID parentTaskId, @RequestBody List<@Valid TaskDto> subTask){
        return sendSuccessfulApiResponse(taskService.createSubtask(email,parentTaskId,subTask),"Sub-Task assigned and created.");
    }
    @DeleteMapping("{taskId}")
    public Task deleteTask(@PathVariable UUID taskId, String email){
        return taskService.deleteTask(taskId, email);
    }

}
