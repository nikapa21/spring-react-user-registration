package org.consoleconnect.spring_react_user_registration;

import static org.mockito.Mockito.mock;

import org.consoleconnect.spring_react_user_registration.service.EmailService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public EmailService emailService() {

        return mock(EmailService.class); // Return a mock EmailService
    }
}
