package com.example.emailsender.service;

public interface EmailService {

    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachment(String name, String to, String token);
    void sendMimeMessageWithEmbeddedFiles(String name, String to, String token);
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);

}
