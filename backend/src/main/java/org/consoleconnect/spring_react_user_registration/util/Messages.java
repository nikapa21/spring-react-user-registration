package org.consoleconnect.spring_react_user_registration.util;

public class Messages {

    // Private constructor to prevent instantiation
    private Messages() {

        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String EMAIL_WELCOME_MESSAGE = "Hello %s,\n\nWelcome to our service! We're glad to have you with us.\n\nBest regards,\nThe Team";
    public static final String EMAIL_UPDATE_NOTIFICATION = "Your email address has been updated from %s to %s.";

    // ERROR
    public static final String USER_NOT_FOUND = "User with ID %d not found";
    public static final String USERS_NOT_FOUND = "Users not found with IDs: %s";
    public static final String UNIQUE_CONSTRAINT_VIOLATION = "A user with this email already exists";
    public static final String EMAIL_SENDING_FAILED = "Failed to send email to %s";
}