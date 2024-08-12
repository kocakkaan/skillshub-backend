package com.reply.skillshub.base.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.reply.skillshub.data.EmailRequest;


@SpringBootTest(classes = EmailService.class)
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testEmailSend() {
        var emailRequest = new EmailRequest();
        emailRequest.setMessage("Test");
        emailRequest.setRecipient("maurits.de.roover@outlook.com");
        emailRequest.setSubject("Test");
        emailService.sendEmail(emailRequest);
    }
    
}
