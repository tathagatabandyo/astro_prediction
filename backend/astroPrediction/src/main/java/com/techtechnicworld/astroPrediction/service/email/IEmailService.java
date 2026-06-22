package com.techtechnicworld.astroPrediction.service.email;

public interface IEmailService {
    void sendVerificationEmail(String to, String name, String token);
    void sendPasswordResetEmail(String to, String name, String token);
    void sendNotificationEmail(String to, String subject, String content);
}
