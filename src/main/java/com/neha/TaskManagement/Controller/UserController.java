package com.neha.TaskManagement.Controller;

import com.neha.TaskManagement.BaseStructure.BaseApiStructure;
import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
public class UserController extends BaseApiStructure {
    @Autowired
    private UserService userService;

    //Instead of directly calling the entity, we call UserDto. And we use @Valid annotation with RequestBody annotation.
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid @RequestBody UserDto userDto){
        User newUser = userService.signupUser(userDto);
        UserDto responseDto = UserDto.entityToDto(newUser);
        return sendSuccessfulApiResponse(responseDto, "Successful signup");

//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//        return new ResponseEntity( HttpStatus.OK,(UserDto.entityToDto((userService.signupUser(userDto, adminId)))));
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
    @GetMapping("/email")
    public User getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }
    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable UUID id){
        return userService.deleteUser(id);
    }
}
