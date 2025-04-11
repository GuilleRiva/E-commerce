package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.RectangularShape;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for user registration, retrieval and management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @Operation(summary = "register a new user", description =
    "Allows a new user to create an account in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user registered correctly"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users users){
        Users created= userService.registerUsers(users);
        return ResponseEntity.ok(created);
    }



    @Operation(summary = "Get user by email", description =
    "Retrieves a user by their registered email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User with the given email not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<Users>getUsersByEmail(@PathVariable String email){
        Users users= userService.findByEmail(email);
        return ResponseEntity.ok(users);
    }



    @Operation(
            summary = "Get user by username",
            description = "Retrieves a user by their unique username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<Users>getUserByUsername(@PathVariable String username){
        Users users= userService.findByUsername(username);
        return ResponseEntity.ok(users);
    }



    @Operation(
            summary = "Check if an email is already registered",
            description = "Returns true if user with the specified email already exists in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Existence check completed successfully")
    })
    @GetMapping("/email-exists/{email}")
    public ResponseEntity<Boolean>emailExists(@PathVariable String email){
        boolean exists= userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }




    @Operation(
            summary = "List all users",
            description = "Retrieves a list of all registered users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<Users>>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


    

    @Operation(summary = "Delete user by ID",
    description = "Deletes a user from the system based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
