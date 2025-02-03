package com.neha.TaskManagement.Service;

import com.neha.TaskManagement.Dtos.LoginRequestDto;
import com.neha.TaskManagement.Dtos.UserDto;
import com.neha.TaskManagement.Entity.User;
import com.neha.TaskManagement.Repository.UserRepository;
import com.neha.TaskManagement.Security.PasswordEncryption;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public User signupUser(UserDto userDto) {
        //dto to entity
        if(userRepository.existsByEmail(userDto.getEmail())){
            throw new RuntimeException("Email already exists!");
        }
        if (userRepository.existsByUsername(userDto.getUsername())){
            throw new RuntimeException("User already exists!");
        }
        String encryptedPassword = PasswordEncryption.encrypt(userDto.getPassword());
        userDto.setPassword(encryptedPassword);
        return userRepository.save(UserDto.dtoToEntity(userDto));
    }

    @Override
    public User loginUser(LoginRequestDto request) {
        //first check if email or username is present. Check for @.
//        if (request.getEmail().contains("@")) <- its email
        //else its a username. So, find the user accordingly.
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<User> user;
        if(request.getEmail().contains("@")){
            user = userRepository.findByEmail(request.getEmail());
        }
        else {
            user = userRepository.findByUsername(request.getUsername());
        }

        if (user.isPresent()){
            User currentUser = user.get();

            if (currentUser.getPassword().equals(PasswordEncryption.encrypt(request.getPassword()))){
                return currentUser;
            }
            else{
                throw new RuntimeException("Incorrect Password");
            }
        }
        else{
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public UserDto isAdmin(String email) {
        User user = userRepository.findByEmailAndIsAdmin(email, true).orElseThrow(()->new RuntimeException("User not found"));
//        UserDto dto = UserDto.entityToDto(user); <- return this object instead of direct entity class.
        return UserDto.entityToDto(user);
    }
    @Override
    public void logoutUser() {

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
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }
    @Override
    public UserDto updateUser(UUID userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
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

        User updatedUser = userRepository.save(existingUser);
        return UserDto.entityToDto(updatedUser);
    }

    @Override
    @Transactional
    public User deleteUser(UUID id) {
        User userToBeDeleted = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        userRepository.delete(userToBeDeleted);
        return userToBeDeleted;
    }

    @Override
    public User isActive(User user) {
        return null;
    }

}
