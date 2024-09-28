package org.consoleconnect.spring_react_user_registration.exceptions;

import static org.consoleconnect.spring_react_user_registration.util.Messages.EMAIL_SENDING_FAILED;

public class EmailSendingException extends RuntimeException {

    public EmailSendingException(String email) {

        super(String.format(EMAIL_SENDING_FAILED, email));
    }

    public EmailSendingException(String message, Throwable cause) {

        super(message, cause);
    }
}