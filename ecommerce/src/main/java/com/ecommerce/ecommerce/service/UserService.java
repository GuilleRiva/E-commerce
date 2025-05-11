package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.UserRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.UserResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerUsers(UserRequestDTO dto){
        log.info("Attempting to register new user with username: {}",dto.getUsername());

        Users user= new Users();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPass(dto.getPassword());

        Users savedUser= userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public Users findByEmail(String email){
        log.info("Attempting to find user by email: {}",email);

        return userRepository.findByEmail(email)
                .orElseThrow(()-> {
                    log.warn("No user found with email: {}", email);
                   return new ResourceNotFoundException("User not found with email:" + email);
                });

    }

    public Users findByUsername(String username){
        log.info("Attempting to find user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(()-> {
                    log.warn("No user found with username: {}",username);
                   return new ResourceNotFoundException("User not found with username: " + username);
                });

    }

    public boolean emailExists(String email){

        boolean exists = userRepository.existsByEmail(email);
        log.debug("Email existence check for '{}': {}", email, exists);
        return exists;
    }

    public List<Users>getAllUsers(){
        log.info("Fetching retrieves all users");
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        log.info("Attempting to delete user with ID: {}", id);

        if (!userRepository.existsById(id)){
            log.warn("User not found with ID: {}",id);
            throw new ResourceNotFoundException("User not found with ID:" + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully. ID: {}", id);
    }


}
