package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.BaseStructure.ApiResponse;
import com.neha.TaskManagement.BaseStructure.BaseApiStructure;
import com.neha.TaskManagement.Dtos.TaskDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.Task;
import com.neha.TaskManagement.Security.LocalAuthStore;
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
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@RequestBody TaskDto taskdto){
        TaskDto newTask = taskService.createTask(taskdto);
        return sendSuccessfulApiResponse(newTask,"Task Initiated.");
    }

    @GetMapping("{taskId}")
    public ResponseEntity getTaskById(@PathVariable UUID taskId){
        return sendSuccessfulApiResponse(taskService.getTaskById(taskId),"Task viewed.");
    }
    @PostMapping("/subtasks")
    public ResponseEntity getTasksByIds(@RequestBody List<String> taskIds){
        return sendSuccessfulApiResponse(taskService.getTaskByIds(taskIds.stream().map(UUID::fromString).toList()),"All tasks view by ID list.");
    }

    @GetMapping("all")
    public ResponseEntity getAllTasks(){
        return sendSuccessfulApiResponse(taskService.getAllTasks(),"All assigned task for user.");
    }

    @PutMapping("update")
    public ResponseEntity updateTask(@RequestBody TaskDto taskDto){
        return sendSuccessfulApiResponse(taskService.updateTask(taskDto),"Task updated.");
    }
    @PutMapping("/assign")
    public ResponseEntity assignNewUsers(@RequestBody TaskDto assignNewUsers){
        return sendSuccessfulApiResponse(taskService.assignNewUsers(assignNewUsers),"New user(s) assigned");
    }
    @PutMapping("/remove-user")
    public ResponseEntity removeAssignedUser(@RequestBody TaskDto removeUsers){
        return sendSuccessfulApiResponse(taskService.removeAssignedUser(removeUsers),"User removed from assigned task.");
    }
    @PostMapping("{parentTaskId}/create-subtask")
    public ResponseEntity createSubtask(@PathVariable("parentTaskId")UUID parentTaskId, @RequestBody List<TaskDto> subTask){
        return sendSuccessfulApiResponse(taskService.createSubtask(parentTaskId,subTask),"Sub-Task assigned and created.");
    }
    @DeleteMapping("{taskId}")
    public ResponseEntity deleteTask(@PathVariable UUID taskId){
        return sendSuccessfulApiResponse(taskService.deleteTask(taskId),"Task deleted.");
    }
    @GetMapping("user")
    public ResponseEntity getTaskByUser(@RequestParam(value = "username",required = false) String username){
        //Will convert it to token based.
        String userid = username==null?LocalAuthStore.getLocalAuthStore().getJwtUser().getPrincipal():username;
        return sendSuccessfulApiResponse(taskService.getTaskByUser(userid),"Task list view for "+userid);
    }
}
