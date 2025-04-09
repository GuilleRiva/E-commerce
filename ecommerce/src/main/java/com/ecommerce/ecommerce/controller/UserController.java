package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users users){
        Users created= userService.registerUsers(users);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Users>getUsersByEmail(@PathVariable String email){
        Users users= userService.findByEmail(email);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Users>getUserByUsername(@PathVariable String username){
        Users users= userService.findByUsername(username);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/email-exists/{email}")
    public ResponseEntity<Boolean>emailExists(@PathVariable String email){
        boolean exists= userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping
    public ResponseEntity<List<Users>>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
