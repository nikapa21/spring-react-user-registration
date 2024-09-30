package org.consoleconnect.spring_react_user_registration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.consoleconnect.spring_react_user_registration.exceptions.UniqueConstraintViolationException;
import org.consoleconnect.spring_react_user_registration.exceptions.UserNotFoundException;
import org.consoleconnect.spring_react_user_registration.model.User;
import org.consoleconnect.spring_react_user_registration.repository.UserRepository;
import org.consoleconnect.spring_react_user_registration.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {

        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", false);
        User user2 = new User(2L, "Jane", "Doe", "jane.doe@example.com", false);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_UserExists() {

        User user = new User(1L, "John", "Doe", "john.doe@example.com", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testSave_User() {

        User user = new User(null, "John", "Doe", "john.doe@example.com", false);
        User savedUser = new User(1L, "John", "Doe", "john.doe@example.com", false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendWelcomeEmail("john.doe@example.com", "John");
    }

    @Test
    void testSave_UserUniqueConstraintViolation() {

        User user = new User(null, "John", "Doe", "john.doe@example.com", false);
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UniqueConstraintViolationException.class, () -> userService.save(user));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateById_UserExists() {

        User existingUser = new User(1L, "John", "Doe", "john.doe@example.com", false);
        User updatedUser = new User(1L, "John", "Smith", "john.smith@example.com", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateById(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Smith", result.getLastName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
        verify(emailService, times(1)).sendEmailUpdateNotification("john.doe@example.com", "john.smith@example.com");
    }

    @Test
    void testUpdateById_UserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateById(1L, new User()));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateByIds_AllUsersExist() {

        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", false);
        User user2 = new User(2L, "Jane", "Doe", "jane.doe@example.com", false);
        List<User> usersToUpdate = Arrays.asList(
                new User(1L, "John", "Smith", "john.smith@example.com", false),
                new User(2L, "Jane", "Smith", "jane.smith@example.com", false)
        );

        when(userRepository.findAllById(anyList())).thenReturn(Arrays.asList(user1, user2));
        when(userRepository.saveAll(anyList())).thenReturn(usersToUpdate);

        List<User> updatedUsers = userService.updateByIds(usersToUpdate);

        assertEquals(2, updatedUsers.size());
        verify(userRepository, times(1)).findAllById(anyList());
        verify(userRepository, times(1)).saveAll(anyList());
        verify(emailService, times(2)).sendEmailUpdateNotification(anyString(), anyString());
    }

    @Test
    void testUpdateByIds_SomeUsersNotFound() {

        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", false);
        List<User> usersToUpdate = Arrays.asList(
                new User(1L, "John", "Smith", "john.smith@example.com", false),
                new User(3L, "Jake", "Doe", "jake.doe@example.com", false)
        );

        when(userRepository.findAllById(anyList())).thenReturn(Arrays.asList(user1));

        assertThrows(UserNotFoundException.class, () -> userService.updateByIds(usersToUpdate));
        verify(userRepository, times(1)).findAllById(anyList());
    }

    @Test
    void testDeactivateById_UserExists() {

        User user = new User(1L, "John", "Doe", "john.doe@example.com", false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User deactivatedUser = userService.deactivateById(1L);

        assertTrue(deactivatedUser.getIsDeactivated());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeactivateById_UserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deactivateById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeactivateUsersByIds_AllUsersExist() {

        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", false);
        User user2 = new User(2L, "Jane", "Doe", "jane.doe@example.com", false);
        List<Long> ids = Arrays.asList(1L, 2L);

        when(userRepository.findAllById(ids)).thenReturn(Arrays.asList(user1, user2));

        userService.deactivateUsersByIds(ids);

        assertTrue(user1.getIsDeactivated());
        assertTrue(user2.getIsDeactivated());
        verify(userRepository, times(1)).findAllById(ids);
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testDeactivateUsersByIds_SomeUsersNotFound() {

        User user1 = new User(1L, "John", "Doe", "john.doe@example.com", false);
        List<Long> ids = Arrays.asList(1L, 3L);

        when(userRepository.findAllById(ids)).thenReturn(Arrays.asList(user1));

        assertThrows(UserNotFoundException.class, () -> userService.deactivateUsersByIds(ids));
        verify(userRepository, times(1)).findAllById(ids);
    }

    @Test
    void testActivateById_UserExists() {

        User user = new User(1L, "John", "Doe", "john.doe@example.com", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.activateById(1L);

        assertFalse(user.getIsDeactivated());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testActivateById_UserNotFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.activateById(1L));
        verify(userRepository, times(1)).findById(1L);
    }
}