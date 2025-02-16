package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;

import com.neha.TaskManagement.Entity.UserRole;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Exception.ConflictException;
import com.neha.TaskManagement.Exception.NotFoundException;
import com.neha.TaskManagement.Exception.UnauthorizedException;
import com.neha.TaskManagement.Model.LoginRequest;
import com.neha.TaskManagement.Model.LoginResponse;
import com.neha.TaskManagement.Repository.TaskRepository;
import com.neha.TaskManagement.Repository.UserRoleRepository;
import com.neha.TaskManagement.Repository.UserRepository;
import com.neha.TaskManagement.Security.Jwt.JwtOperationWrapperSvc;
import com.neha.TaskManagement.Security.PasswordEncryption;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private JwtOperationWrapperSvc jwtOperationWrapperSvc;

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
        //generating automated username.
        userDto.setUsername(generateUsername(userDto.getFirstName(),userDto.getLastName()));
        String encryptedPassword = PasswordEncryption.encrypt(userDto.getPassword());
        userDto.setPassword(encryptedPassword);
        User toBeSaved = UserDto.dtoToEntity(userDto);
        toBeSaved.setRoles(new HashSet<>());

        if (userDto.getRoles()!=null && !userDto.getRoles().isEmpty()){
            userDto.getRoles().forEach(role -> {
                UserRole r = userRoleRepository.findByRoleCode(role)
                        .orElse(null);
                if (r!=null){
                    toBeSaved.getRoles().add(r);
                }
            });
        } else {
            UserRole guestRole = userRoleRepository.findByRoleCode("GUEST_USER")
                    .orElseThrow(()->new NotFoundException("Guest role not found."));
            toBeSaved.getRoles().add(guestRole);
        }
        return UserDto.entityToDto(userRepository.save(toBeSaved));
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        //first check if email or username is present. Check for @.
//        if (request.getEmail().contains("@")) <- its email
        //else its a username. So, find the user accordingly.
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        User user;
        if(loginRequest.getUserId().contains("@")){
            user = userRepository.findByEmail(loginRequest.getUserId())
                    .orElseThrow(()->new NotFoundException("User does not exists."));
        }
        else {
            user = userRepository.findByUsername(loginRequest.getUserId())
                    .orElseThrow(()->new NotFoundException("User does not exists."));
        }

        if (user.getPassword().equals(PasswordEncryption.encrypt(loginRequest.getPassword()))){
            UserDto authenticatedUser = UserDto.entityToDto(user);
            String token = jwtOperationWrapperSvc.generateToken(authenticatedUser);
            String refreshToken = jwtOperationWrapperSvc.generateRefreshToken(authenticatedUser);

            return new LoginResponse(token,refreshToken,authenticatedUser);
        }
        throw new UnauthorizedException("Wrong username/password. Please try again.");
    }

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

    private String generateUsername(String firstName, String lastName){
        firstName = firstName.replaceAll("\\s+", "").trim().toLowerCase();
        lastName = lastName.replaceAll("\\s+", "").trim().toLowerCase();
        String username = firstName.charAt(0)+lastName;

        if (!userRepository.existsByUsername(username)){
            return username;
        }

        int firsNameLen = firstName.length();
        int index = 1;
        int addOns = 1;
        String baseUsername = firstName + lastName;
        while(userRepository.existsByUsername(username)){
            if (index<firsNameLen){
                username = firstName.substring(0,index+1)+lastName;
                index++;
            }else{
                username = baseUsername + String.format("%02d",addOns);
                addOns++;
            }
        }
        return username;
    }

}
