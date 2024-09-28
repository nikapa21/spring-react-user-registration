package org.consoleconnect.spring_react_user_registration.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.consoleconnect.spring_react_user_registration.exceptions.UniqueConstraintViolationException;
import org.consoleconnect.spring_react_user_registration.exceptions.UserNotFoundException;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.repository.UserRepository;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {

        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User save(User user) {

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public User updateById(Long id, User user) {

        User managedUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        managedUser.setFirstName(user.getFirstName());
        managedUser.setLastName(user.getLastName());
        managedUser.setEmail(user.getEmail());

        try {
            return userRepository.save(managedUser);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public List<User> updateByIds(List<User> usersToUpdate) {

        List<Long> ids = usersToUpdate.stream().map(User::getId).toList();
        List<User> existingUsers = userRepository.findAllById(ids);
        Set<Long> foundIds = existingUsers.stream().map(User::getId).collect(Collectors.toSet());
        List<Long> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

        if (!missingIds.isEmpty()) {
            throw new UserNotFoundException(missingIds);
        }

        existingUsers.forEach(existingUser -> usersToUpdate.stream()
                .filter(user -> user.getId().equals(existingUser.getId()))
                .findFirst()
                .ifPresent(user -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setEmail(user.getEmail());
                }));

        try {
            return userRepository.saveAll(existingUsers);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public void softDeleteById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void softDeleteUsersByIds(List<Long> ids) {

        List<User> users = userRepository.findAllById(ids);
        List<Long> foundIds = users.stream().map(User::getId).toList();
        List<Long> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

        if (!missingIds.isEmpty()) {
            throw new UserNotFoundException(missingIds);
        }

        users.forEach(user -> user.setIsDeleted(true));
        userRepository.saveAll(users);
    }
}
