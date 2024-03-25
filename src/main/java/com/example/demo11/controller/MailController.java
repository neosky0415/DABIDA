package com.example.demo11.controller;

import com.example.demo11.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MailController {

    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @ResponseBody
    @PostMapping("/mail")
    public String sendEmail(String username) {

        return emailService.sendMail(username);
    }

}