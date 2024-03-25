package com.example.demo11.service;

import com.example.demo11.config.EmailConfiguration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailConfiguration emailConfiguration;

    public EmailService(
            JavaMailSender javaMailSender,
            EmailConfiguration emailConfiguration
    ) {
        this.javaMailSender = javaMailSender;
        this.emailConfiguration = emailConfiguration;
    }

    private static String number;

    public static void createNumber() {
        number = RandomStringUtils.randomNumeric(6);
    }
    public MimeMessage CreateMessage(String username){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();



        try {
            message.setFrom(emailConfiguration.getUsername());
            message.setRecipients(MimeMessage.RecipientType.TO, username);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }
    public String sendMail(String username){

        MimeMessage message = CreateMessage(username);

        javaMailSender.send(message);

        return number;
    }
}
