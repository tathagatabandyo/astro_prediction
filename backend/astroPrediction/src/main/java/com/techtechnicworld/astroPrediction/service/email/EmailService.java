package com.techtechnicworld.astroPrediction.service.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    @Value("${domain}")
    private String domainUrl;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Override
    @Async
    public void sendVerificationEmail(String to, String name, String token) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromName + " <" + fromEmail + ">");
            msg.setTo(to);
            msg.setSubject("Verify Your Email - AstroPrediction");
            msg.setText("Hello " + name + ",\n\nPlease verify your email by clicking: " +
                    domainUrl + "/verify-email?token=" + token +
                    "\n\nThis link expires in 24 hours.\n\nAstroPrediction Team");
            javaMailSender.send(msg);
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
        }

    }

    @Override
    @Async
    public void sendPasswordResetEmail(String to, String name, String token) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromName + " <" + fromEmail + ">");
            msg.setTo(to);
            msg.setSubject("Verify Your Email - AstroPrediction");
            msg.setText("Hello " + name + ",\n\nReset your password: " +
                    domainUrl + "/reset-password?token=" + token +
                    "\n\nThis link expires in 1 hour.\n\nAstroPrediction Team");
            javaMailSender.send(msg);
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void sendNotificationEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromName + " <" + fromEmail + ">");
            msg.setTo(to);
            msg.setSubject("Verify Your Email - AstroPrediction");
            msg.setText(content);
            javaMailSender.send(msg);
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
        }
    }

}
