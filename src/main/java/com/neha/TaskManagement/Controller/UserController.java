package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    //Instead of directly calling the entity, we call UserDto. And we use @Valid annotation with RequestBody annotation.
    @PostMapping("/signup")
    public UserDto signupUser(@Valid @RequestBody UserDto userDto){
        return UserDto.entityToDto(userService.signupUser(userDto));
    }
    @PostMapping("/login")
    public UserDto loginUser(@Valid @RequestBody LoginRequestDto requestDto){
        return UserDto.entityToDto(userService.loginUser(requestDto));
    }
    @PutMapping("/update/{userId}")
    public UserDto updateUser(@PathVariable UUID userId, @Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @GetMapping("/isAdmin")
    public UserDto isAdmin(@Valid @RequestParam String email){
        return userService.isAdmin(email);
    }
    @GetMapping("/all")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id){
        return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id){
        return userService.deleteUser(id);
    }
}
