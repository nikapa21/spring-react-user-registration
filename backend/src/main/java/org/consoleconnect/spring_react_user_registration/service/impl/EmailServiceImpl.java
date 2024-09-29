package org.consoleconnect.spring_react_user_registration.service.impl;

import static org.consoleconnect.spring_react_user_registration.util.Messages.EMAIL_UPDATE_NOTIFICATION;
import static org.consoleconnect.spring_react_user_registration.util.Messages.EMAIL_WELCOME_MESSAGE;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.consoleconnect.spring_react_user_registration.exceptions.EmailSendingException;
import org.consoleconnect.spring_react_user_registration.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final WebClient webClient;

    public EmailServiceImpl(JavaMailSender mailSender, WebClient.Builder webClientBuilder) {

        this.mailSender = mailSender;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8025/api").build();
        logger.info("EmailServiceImpl initialized with JavaMailSender and WebClient.");
    }

    @Override
    public void sendWelcomeEmail(String to, String firstName) {

        logger.debug("Preparing to send welcome email to: {}", to);
        sendEmail(to, "Welcome to Our Service", String.format(EMAIL_WELCOME_MESSAGE, firstName));
        logger.info("Welcome email sent to: {}", to);
    }

    @Override
    public void sendEmailUpdateNotification(String oldEmail, String newEmail) {

        logger.debug("Preparing to send email update notification from: {} to: {}", oldEmail, newEmail);
        String subject = "Your Email Address Has Been Updated";
        String message = String.format(EMAIL_UPDATE_NOTIFICATION, oldEmail, newEmail);
        sendEmail(oldEmail, subject, message);
        sendEmail(newEmail, subject, message);
        logger.info("Email update notification sent to old: {} and new: {} email addresses.", oldEmail, newEmail);
    }

    private void sendEmail(String to, String subject, String message) {

        try {
            logger.debug("Creating MimeMessage for: {}", to);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
            logger.info("Email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
            throw new EmailSendingException(to, e);
        }
    }

    @Override
    public Mono<String> fetchSentEmails() {

        logger.debug("Fetching sent emails from MailHog.");
        return webClient.get()
                .uri("/v2/messages")
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("Successfully fetched emails from MailHog."))
                .onErrorResume(WebClientResponseException.class, e -> {
                    logger.error("Error fetching emails from MailHog: {}", e.getMessage());
                    return Mono.just("[]");
                })
                .onErrorResume(Exception.class, e -> {
                    logger.error("Unexpected error: {}", e.getMessage());
                    return Mono.just("[]");
                });
    }
}