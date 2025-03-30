package com.reply.skillshub.base.services;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.thymeleaf.context.Context;

import com.reply.skillshub.data.EmailRequest;

@Import(ThymeleafTestConfig.class)
@SpringBootTest(classes = { EmailService.class })
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @Ignore
    void testEmailSend() {
        var emailRequest = new EmailRequest();
        var context = new Context();
        context.setVariable("title", "Is this a title?");
        context.setVariable("link", "https://chat.mistral.ai");
        context.setVariable("username", "Maurits De Roover");
        emailRequest.setTemplate("confirmation");
        emailRequest.setRecipient("maurits.de.roover@outlook.com");
        emailRequest.setSubject("Test");
        emailRequest.setContext(context);
        emailService.sendEmail(emailRequest);
    }

}
