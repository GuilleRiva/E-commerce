package com.ecommerce.ecommerce.service;

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

    public Users registerUsers(Users newUser){
        if (userRepository.existsByEmail(newUser.getEmail())){
            throw new IllegalArgumentException("The email is no longer in use");
        }
        return userRepository.save(newUser);
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
