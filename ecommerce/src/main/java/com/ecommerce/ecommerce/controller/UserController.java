package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.dto.XRequestDTO.UserRequestDTO;
import com.ecommerce.ecommerce.dto.XResponseDTO.UserResponseDTO;
import com.ecommerce.ecommerce.model.Users;
import com.ecommerce.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for user registration, retrieval and management")
@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO){

        log.info("Attempting to register user with username: {}",
                userRequestDTO.getUsername());

        UserResponseDTO created= userService.registerUsers(userRequestDTO);

        log.info("User registered successfully: {}",
                created.getUsername());

        return ResponseEntity.ok(created);
    }



    @Operation(summary = "Get user by email", description =
    "Retrieves a user by their registered email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User with the given email not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @GetMapping("/email/{email}")
    public ResponseEntity<Users>getUsersByEmail(@PathVariable String email){

        log.info("Fetching user by username: {}", email);

        Users users= userService.findByEmail(email);

        log.info("User found: {}", users.getUsername());

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
    @PreAuthorize("hasAnyRole('ADMIN, SELLER')")
    @GetMapping("/username/{username}")
    public ResponseEntity<Users>getUserByUsername(@PathVariable String username){

        log.info("Fetching user by username: {}", username);

        Users users= userService.findByUsername(username);

        log.info("User found: {}", users.getUsername());

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

        log.info("Checking if email exists: {}", email);

        boolean exists= userService.emailExists(email);

        log.info("Email {} existence: {}", email, exists);

        return ResponseEntity.ok(exists);
    }




    @Operation(
            summary = "List all users",
            description = "Retrieves a list of all registered users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Users>>getAllUsers(){

        log.info("Retrieving all users");

        List<Users> users = userService.getAllUsers();

        log.info("Total users found: {}", users.size());

        return ResponseEntity.ok(users);
    }


    

    @Operation(summary = "Delete user by ID",
    description = "Deletes a user from the system based on their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteUser(@PathVariable Long id){

        log.info("Attempting to delete user with ID: {}", id);

        userService.deleteUser(id);

        log.info("User with ID {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }
}
