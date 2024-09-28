package org.consoleconnect.spring_react_user_registration.service.impl;

import static org.consoleconnect.spring_react_user_registration.util.Messages.EMAIL_UPDATE_NOTIFICATION;
import static org.consoleconnect.spring_react_user_registration.util.Messages.EMAIL_WELCOME_MESSAGE;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.consoleconnect.spring_react_user_registration.exceptions.EmailSendingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String to, String firstName) {

        sendEmail(to, "Welcome to Our Service", String.format(EMAIL_WELCOME_MESSAGE, firstName));
    }

    public void sendEmailUpdateNotification(String oldEmail, String newEmail) {

        String subject = "Your Email Address Has Been Updated";
        String message = String.format(EMAIL_UPDATE_NOTIFICATION, oldEmail, newEmail);
        sendEmail(oldEmail, subject, message); // notify both
        sendEmail(newEmail, subject, message); // old and new mail
    }

    private void sendEmail(String to, String subject, String message) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendingException(to, e);
        }
    }
}