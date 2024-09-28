package org.consoleconnect.spring_react_user_registration.exceptions;

import static org.consoleconnect.spring_react_user_registration.util.ErrorMessages.USERS_NOT_FOUND;
import static org.consoleconnect.spring_react_user_registration.util.ErrorMessages.USER_NOT_FOUND;

import java.util.List;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {

        super(String.format(USER_NOT_FOUND, id));
    }

    public UserNotFoundException(List<Long> ids) {

        super(String.format(USERS_NOT_FOUND, ids));
    }
}
