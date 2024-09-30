package org.consoleconnect.spring_react_user_registration.service.impl;

import static org.consoleconnect.spring_react_user_registration.util.Messages.USER_NOT_FOUND;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.consoleconnect.spring_react_user_registration.exceptions.UniqueConstraintViolationException;
import org.consoleconnect.spring_react_user_registration.exceptions.UserNotFoundException;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.repository.UserRepository;
import org.consoleconnect.spring_react_user_registration.service.EmailService;
import org.consoleconnect.spring_react_user_registration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {

        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public List<User> findAll() {

        logger.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("Retrieved {} users", users.size());
        return users;
    }

    @Override
    public User findById(Long id) {

        logger.debug("Finding user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(String.format(USER_NOT_FOUND, id));
                    return new UserNotFoundException(id);
                });
    }

    @Override
    public User save(User user) {

        logger.debug("Saving user: {}", user);
        try {
            User registeredUser = userRepository.save(user);
            emailService.sendWelcomeEmail(registeredUser.getEmail(), registeredUser.getFirstName());
            logger.info("User registered successfully: {}", registeredUser);
            return registeredUser;
        } catch (DataIntegrityViolationException e) {
            logger.error("Unique constraint violation for email: {}", user.getEmail(), e);
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    @Transactional
    public User updateById(Long id, User user) {

        logger.debug("Updating user with ID: {}", id);
        User managedUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error(String.format(USER_NOT_FOUND, id));
                    return new UserNotFoundException(id);
                });

        String oldEmail = managedUser.getEmail(); // Store the old email for comparison

        if (user.getFirstName() != null) {
            managedUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            managedUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            managedUser.setEmail(user.getEmail());
        }

        try {
            User updatedUser = userRepository.save(managedUser);
            // Send an update email if the email address has changed
            if (!oldEmail.equals(managedUser.getEmail())) {
                emailService.sendEmailUpdateNotification(oldEmail, managedUser.getEmail());
            }
            logger.info("User updated successfully: {}", updatedUser);
            return updatedUser;
        } catch (DataIntegrityViolationException e) {
            logger.error("Unique constraint violation for email: {}", user.getEmail(), e);
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    @Transactional
    public List<User> updateByIds(List<User> usersToUpdate) {

        logger.debug("Updating multiple users: {}", usersToUpdate);
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
                    String oldEmail = existingUser.getEmail(); // Store the old email for comparison
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setEmail(user.getEmail());

                    // Send email notification if the email has changed
                    if (!oldEmail.equals(existingUser.getEmail())) {
                        emailService.sendEmailUpdateNotification(oldEmail, existingUser.getEmail());
                    }
                }));

        try {
            List<User> updatedUsers = userRepository.saveAll(existingUsers);
            logger.info("Users updated successfully: {}", updatedUsers);
            return updatedUsers;
        } catch (DataIntegrityViolationException e) {
            logger.error("Unique constraint violation during batch update", e);
            throw new UniqueConstraintViolationException();
        }
    }

    @Override
    public User deactivateById(Long id) {

        logger.debug("Deactivating user with ID: {}", id);
        User managedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        managedUser.setIsDeactivated(true);
        User updatedUser = userRepository.save(managedUser);
        logger.info("User with ID {} has been deactivated", id);
        return updatedUser;
    }

    @Override
    public List<User> deactivateUsersByIds(List<Long> ids) {

        logger.debug("Deactivating users with IDs: {}", ids);
        List<User> users = userRepository.findAllById(ids);
        List<Long> foundIds = users.stream().map(User::getId).toList();
        List<Long> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

        if (!missingIds.isEmpty()) {
            throw new UserNotFoundException(missingIds);
        }

        users.forEach(user -> user.setIsDeactivated(true));
        List<User> deactivatedUsers = userRepository.saveAll(users);
        logger.info("Users with IDs {} have been deactivated", ids);
        return deactivatedUsers;
    }

    @Override
    @Transactional
    public void activateById(Long id) {

        logger.debug("Activating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setIsDeactivated(false);
        userRepository.save(user);
        logger.info("User with ID {} has been activated", id);
    }
}