package com.example.emailsender.service.impl;

import com.example.emailsender.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.net.URL;

import static com.example.emailsender.util.EmailUtils.getLinkToValidate;
import static com.example.emailsender.util.EmailUtils.getMessage;

@Service
@RequiredArgsConstructor
public class GoogleEmailServiceImpl implements EmailService {

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "emailTemplate";
    public static final String TEXT_HTML_ENCODING = "text/html";

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.verify.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachment(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getMessage(name, host, token));

            URL firstImg = getClass().getClassLoader().getResource("images/first.jpg");
            URL secondImg = getClass().getClassLoader().getResource("images/second.png");
            FileSystemResource firstFile = new FileSystemResource(new File(firstImg.toURI()));
            FileSystemResource secondFile = new FileSystemResource(new File(secondImg.toURI()));

            helper.addAttachment(firstFile.getFilename(), firstFile);
            helper.addAttachment(secondFile.getFilename(), secondFile);

            emailSender.send(message);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(getMessage(name, host, token));

            URL firstImg = getClass().getClassLoader().getResource("images/first.jpg");
            URL secondImg = getClass().getClassLoader().getResource("images/second.png");
            FileSystemResource firstFile = new FileSystemResource(new File(firstImg.toURI()));
            FileSystemResource secondFile = new FileSystemResource(new File(secondImg.toURI()));

            helper.addInline("<" + firstFile.getFilename() + ">", firstFile);
            helper.addInline("<" + secondFile.getFilename() + ">", secondFile);

            emailSender.send(message);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(fromEmail);
            helper.setTo(to);

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", getLinkToValidate(host, token));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            URL firstImg = getClass().getClassLoader().getResource("images/first.jpg");
            BodyPart imageBodyParty = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(new File(firstImg.toURI()));
            imageBodyParty.setDataHandler(new DataHandler(dataSource));
            imageBodyParty.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(imageBodyParty);

            message.setContent(mimeMultipart);
            emailSender.send(message);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }
}
