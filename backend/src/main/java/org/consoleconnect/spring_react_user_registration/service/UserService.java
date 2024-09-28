package org.consoleconnect.spring_react_user_registration.service;

import java.util.List;
import org.consoleconnect.spring_react_user_registration.model.User;

public interface UserService {

    User save(User user);

    User updateById(Long id, User user);

    List<User> updateByIds(List<User> usersToUpdate);

    List<User> findAll();

    User findById(Long id);

    void softDeleteById(Long id);

    void softDeleteUsersByIds(List<Long> ids);
}