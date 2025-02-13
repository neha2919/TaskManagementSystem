package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;

import com.neha.TaskManagement.Entity.UserRole;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Exception.ConflictException;
import com.neha.TaskManagement.Exception.NotFoundException;
import com.neha.TaskManagement.Exception.UnauthorizedException;
import com.neha.TaskManagement.Repository.UserRoleRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import com.neha.TaskManagement.Security.PasswordEncryption;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDto signupUser(@Valid UserDto userDto) {
        //dto to entity
//        User admin = userRepository.findById(adminId).orElseThrow(()->new RuntimeException("Admin not found"));
//        if(!Boolean.TRUE.equals(admin.getIsAdmin())){
//            throw new RuntimeException("Only admin can create new user");
//        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new ConflictException("Email already exists!");
        }
        if (userRepository.existsByUsername(userDto.getUsername())){
            throw new ConflictException("User already exists!");
        }
        String encryptedPassword = PasswordEncryption.encrypt(userDto.getPassword());
        userDto.setPassword(encryptedPassword);

        User newUser = userRepository.save(UserDto.dtoToEntity(userDto));

        return UserDto.entityToDto(newUser);
    }

    @Override
    public UserDto loginUser(LoginRequestDto request) {
        //first check if email or username is present. Check for @.
//        if (request.getEmail().contains("@")) <- its email
        //else its a username. So, find the user accordingly.
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        User user;
        if(request.getEmail().contains("@")){
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()->new NotFoundException("User does not exists."));
        }
        else {
            user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(()->new NotFoundException("User does not exists."));
        }

        if (user.getPassword().equals(PasswordEncryption.encrypt(request.getPassword()))){
            return UserDto.entityToDto(user);
        }
        throw new UnauthorizedException("Wrong username/password. Please try again.");
    }

//    @Override
    /*public UserDto isAdmin(String email) {
        User user = userRepository.findByEmailAndIsAdmin(email, true)
                .orElseThrow(()->new NotFoundException("User does not exists."));
//        UserDto dto = UserDto.entityToDto(user); <- return this object instead of direct entity class.
        return UserDto.entityToDto(user);
    }*/

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        //new List of UserDto
        List<UserDto> userDto = new ArrayList<>();
        //loop the list of user entity
        //convert the entity to dto and then add that single object to the newly created dto list.
        for (User user : users ){
            userDto.add(UserDto.entityToDto(user));
        }
        return userDto;
    }
    @Override
    public UserDto getUserByEmail(String email) {
        return UserDto.entityToDto(userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User does not exists.")));
    }
    @Override
    public UserDto updateUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new NotFoundException("User does not exists."));
        if(userDto.getUsername() != null && !userDto.getUsername().isEmpty()){
            existingUser.setUsername(userDto.getUsername());
        }
        if(userDto.getEmail() != null && !userDto.getEmail().isEmpty()){
            existingUser.setEmail(userDto.getEmail());
        }
        if(userDto.getPhoneNumber() != null){
            existingUser.setPhoneNumber(userDto.getPhoneNumber());
        }
        if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
            String encryptedPassword = PasswordEncryption.encrypt(userDto.getPassword());
            existingUser.setPassword(encryptedPassword);
        }
        if (userDto.getFirstName() != null && !userDto.getFirstName().isEmpty()) {
            existingUser.setFirstName(userDto.getFirstName());
        }

        if (userDto.getLastName() != null && !userDto.getLastName().isEmpty()) {
            existingUser.setLastName(userDto.getLastName());
        }

        existingUser.setFullName();
//        existingUser.setIsAdmin(userDto.getIsAdmin());

        User updatedUser = userRepository.save(existingUser);
        return UserDto.entityToDto(updatedUser);
    }

    @Override
    public List<UserDto> getAssignedUsersByTask(UUID taskId) {
        return userRepository.findByTasks_TaskId(taskId)
                .orElse(new ArrayList<>())
                .stream().map(UserDto::entityToDto).toList();
    }

    @Override
    @Transactional
    //change this to deactivate user
    public User deleteUser(UUID id) {
        User userToBeDeleted = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("User does not exists."));
        userRepository.delete(userToBeDeleted);
        return userToBeDeleted;
    }

    @Override
    public UserDto assignRoleToUser(String email, String roleName){
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        UserRole userRole = userRoleRepository.findByRoleName(roleName).orElseThrow(()->new NotFoundException("Role not found"));

        user.getRoles().add(userRole);
        User updatedUser = userRepository.save(user);

        return UserDto.entityToDto(updatedUser);
    }
    @Override
    public void logoutUser() {

    }
    @Override
    public User isActive(User user) {
        return null;
    }

}
