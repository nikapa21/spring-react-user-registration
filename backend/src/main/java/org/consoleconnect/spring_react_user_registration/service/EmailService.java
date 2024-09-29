package org.consoleconnect.spring_react_user_registration.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface EmailService {

    void sendWelcomeEmail(String to, String firstName);

    void sendEmailUpdateNotification(String oldEmail, String newEmail);

    Mono<String> fetchSentEmails();
}