package com.journal.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.from:no-reply@journal-app.local}")
    private String fromAddress;

    public boolean sendPasswordResetEmail(String to, String resetLink) {
        try {
            if (javaMailSender == null) {
                log.warn("MailSender not configured. Skipping email send to {} with link {}", to, resetLink);
                return false;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject("Reset your Journal App password");
            message.setText("Click the link to reset your password: " + resetLink);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            log.error("Failed to send reset email to {}", to, e);
            return false;
        }
    }

    public void sendMail(String to, String subject, String body){
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
        }catch (Exception e){
            log.error("Exception while sendMail", e);
        }
    }
}
