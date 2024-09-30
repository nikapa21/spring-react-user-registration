package org.consoleconnect.spring_react_user_registration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    public ResponseEntity<List<User>> findAll() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique ID")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Register a new user and send a welcome email")
    public ResponseEntity<User> create(@RequestBody @Valid User user) {

        User registeredUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user's information")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody @Valid User user) {

        User updatedUser = userService.updateById(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping
    @Operation(summary = "Update multiple users", description = "Update information for multiple users")
    public ResponseEntity<List<User>> updateMultiple(@RequestBody @Valid List<User> users) {

        List<User> updatedUsers = userService.updateByIds(users);
        return ResponseEntity.ok(updatedUsers);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate a user by their ID")
    public ResponseEntity<User> deactivate(@PathVariable Long id) {

        User updatedUser = userService.deactivateById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("deactivate")
    @Operation(summary = "Deactivate multiple users", description = "Deactivate multiple users by their IDs")
    public ResponseEntity<List<User>> deactivateMultiple(@RequestBody List<Long> ids) {

        List<User> deactivatedUsers = userService.deactivateUsersByIds(ids);
        return ResponseEntity.ok(deactivatedUsers);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a user by their ID")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {

        userService.activateById(id);
        return ResponseEntity.noContent().build();
    }
}
