package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.dto.XRequestDTO.UserRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.UserResponseDTO;
import com.ecommerce.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerUsers(UserRequestDTO dto){
        Users user= new Users();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPass(dto.getPassword());

        Users savedUser= userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public Users findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email:" + email));
    }

    public Users findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with username: " + username));
    }

    public boolean emailExists(String email){
        return userRepository.existsByEmail(email);
    }

    public List<Users>getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with ID:" + id);
        }
        userRepository.deleteById(id);
    }


}
