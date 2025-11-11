package com.example.cash_flow_backend.budgettracker.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailAndPDFService {
    private JavaMailSender sender;

    public EmailAndPDFService(JavaMailSender sender) {
        this.sender = sender;
    }

    public void simpleSentEmail(String email, String subject, String text) throws Exception {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("davidstupenarima@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        this.sender.send(mailMessage);
    }
}
