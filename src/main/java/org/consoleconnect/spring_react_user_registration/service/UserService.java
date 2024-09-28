package org.consoleconnect.spring_react_user_registration.service;

import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;

public interface UserService {

    User save(User user);

    List<User> findAll();

}