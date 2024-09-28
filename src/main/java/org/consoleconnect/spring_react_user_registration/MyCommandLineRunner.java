package org.consoleconnect.spring_react_user_registration;

import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final UserService userService;

    public MyCommandLineRunner(UserService userService) {

        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setFirstName("Nikos");
        user.setLastName("Karagkounis");
        user.setEmail("nikapa21@gmail.com");
        userService.save(user);

        User user2 = new User();
        user2.setFirstName("George");
        user2.setLastName("Pappas");
        user2.setEmail("gpappas@gmail.com");
        userService.save(user2);
    }
}
