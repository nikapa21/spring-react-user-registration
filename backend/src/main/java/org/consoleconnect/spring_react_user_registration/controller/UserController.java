package org.consoleconnect.spring_react_user_registration.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid User user) {

        User registeredUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody @Valid User user) {

        User updatedUser = userService.updateById(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping
    public ResponseEntity<List<User>> updateMultiple(@RequestBody @Valid List<User> users) {

        List<User> updatedUsers = userService.updateByIds(users);
        return ResponseEntity.ok(updatedUsers);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivate(@PathVariable Long id) {

        User updatedUser = userService.deactivateById(id);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("deactivate")
    public ResponseEntity<List<User>> deactivateMultiple(@RequestBody List<Long> ids) {

        List<User> deactivatedUsers = userService.deactivateUsersByIds(ids);
        return ResponseEntity.ok(deactivatedUsers);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {

        userService.activateById(id);
        return ResponseEntity.noContent().build();
    }
}
