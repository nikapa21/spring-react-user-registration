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
    public ResponseEntity<List<User>> findUsers() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody @Valid User user) {

        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody @Valid User user) {

        User updatedUser = userService.updateById(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping
    public ResponseEntity<List<User>> updateUsers(@RequestBody List<User> users) {

        List<User> updatedUsers = userService.updateByIds(users);
        return ResponseEntity.ok(updatedUsers);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {

        userService.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteUsers")
    public ResponseEntity<Void> deleteUsers(@RequestBody List<Long> ids) {

        userService.softDeleteUsersByIds(ids);
        return ResponseEntity.noContent().build();
    }
}
