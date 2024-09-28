package org.consoleconnect.spring_react_user_registration.exceptions;

import static org.consoleconnect.spring_react_user_registration.util.Messages.UNIQUE_CONSTRAINT_VIOLATION;

public class UniqueConstraintViolationException extends RuntimeException {

    public UniqueConstraintViolationException() {

        super(UNIQUE_CONSTRAINT_VIOLATION);
    }
}
