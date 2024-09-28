package org.consoleconnect.spring_react_user_registration.service.impl;

import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.repository.UserRepository;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

}
