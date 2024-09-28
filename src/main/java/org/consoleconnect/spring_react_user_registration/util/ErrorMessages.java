package org.consoleconnect.spring_react_user_registration.util;

public class ErrorMessages {

    // Private constructor to prevent instantiation
    private ErrorMessages() {

        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String USER_NOT_FOUND = "User with ID %d not found";
    public static final String USERS_NOT_FOUND = "Users not found with IDs: %s";
    public static final String UNIQUE_CONSTRAINT_VIOLATION = "A user with this email already exists";
}