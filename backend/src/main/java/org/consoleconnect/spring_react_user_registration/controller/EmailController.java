package org.consoleconnect.spring_react_user_registration.controller;

import org.consoleconnect.spring_react_user_registration.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {

        this.emailService = emailService;
    }

    @GetMapping
    public Mono<String> getEmails() {

        return emailService.fetchSentEmails();
    }
}