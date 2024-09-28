package org.consoleconnect.spring_react_user_registration.controller;

import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<User> findAll() {

        return userService.findAll();
    }
}
