package com.springbite.authorization_server.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.support}")
    private String supportEmail;

    @Value("${website.url}")
    private String websiteUrl;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendConfirmationEmail(String to, String firstname, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("code", code);
        context.setVariable("supportEmail", supportEmail);
        context.setVariable("websiteUrl", websiteUrl);

        String emailContent = templateEngine.process("confirmation-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(fromEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Confirm Your Registration with Spring Bite");
        message.setContent(emailContent, "text/html");

        mailSender.send(message);
    }

    @Async
    public void sendResetPasswordEmail(String to, String firstname, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("code", code);
        context.setVariable("supportEmail", supportEmail);
        context.setVariable("websiteUrl", websiteUrl);

        String emailContent = templateEngine.process("password-reset-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(fromEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Reset Your Password");
        message.setContent(emailContent, "text/html");

        mailSender.send(message);
    }
}
