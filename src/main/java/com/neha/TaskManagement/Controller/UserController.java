package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.BaseStructure.ApiResponse;
import com.neha.TaskManagement.BaseStructure.BaseApiStructure;
import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Model.LoginRequest;
import com.neha.TaskManagement.Model.LoginResponse;
import com.neha.TaskManagement.Security.LocalAuthStore;
import com.neha.TaskManagement.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("api/users")
public class UserController extends BaseApiStructure {
    @Autowired
    private UserService userService;

    //Instead of directly calling the entity, we call UserDto. And we use @Valid annotation with RequestBody annotation. - done
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signupUser(@Valid @RequestBody UserDto userDto) {
        return sendSuccessfulApiResponse(userService.signupUser(userDto), "Successful signup");
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        return sendSuccessfulApiResponse(userService.loginUser(loginRequest), "Successful login");
    }
    //we do not need any userid as path variable instead, user the userDto for the id/email.
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@Valid @RequestBody UserDto userDto) {
        return sendSuccessfulApiResponse(userService.updateUser(userDto), "User Profile Successfully Updated");
    }
// will be changing the admin logic as per the hierarchy
//    @GetMapping("/isAdmin")
//    public UserDto isAdmin(@Valid @RequestParam String email){
//        return userService.isAdmin(email);
//    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers(){
        return sendSuccessfulApiResponse(userService.getAllUsers(), "List of all users");
    }
    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@RequestParam String email){
        return sendSuccessfulApiResponse(userService.getUserByEmail(email), "User Found");
    }
    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id){
        return userService.deleteUser(id);
    }
    @GetMapping("task/{taskId}/users")
    public ResponseEntity getAllAssignedUsersByTask(@PathVariable("taskId")UUID taskId){
        return sendSuccessfulApiResponse(userService.getAssignedUsersByTask(taskId),"All assigned users for current task.");
    }

}
