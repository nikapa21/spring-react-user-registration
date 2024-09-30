package org.consoleconnect.spring_react_user_registration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.consoleconnect.spring_react_user_registration.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Email Management", description = "Operations related to email management")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {

        this.emailService = emailService;
    }

    @GetMapping
    @Operation(summary = "Get sent emails", description = "Retrieve a list of all sent emails")
    public Mono<String> getEmails() {

        return emailService.fetchSentEmails();
    }
}