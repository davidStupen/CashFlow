package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.exception.ValidationEmailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailAndPDFService {
    private JavaMailSender sender;

    public EmailAndPDFService(JavaMailSender sender) {
        this.sender = sender;
    }
    public void validationEmail(String email) throws ValidationEmailException {
        if (email.length() < 6){
            throw new ValidationEmailException("Email is too short " + email);
        }
        if ( ! email.contains("@")){
            throw new ValidationEmailException("The email has to contain @ " + email);
        }
        if ( ! email.contains(".")){
            throw new ValidationEmailException("The email has to contain dot " + email);
        }

        String[] splitEmailByDot = email.split("\\.");
        if (splitEmailByDot[splitEmailByDot.length - 1].length() > 14){
            throw new ValidationEmailException("Invalid email " + email + " with " + splitEmailByDot[splitEmailByDot.length - 1]);
        }

        String[] splitEmailByAt = email.split("@");
        if (splitEmailByAt.length != 2){
            throw new ValidationEmailException("Only one at symbol is allowed. You have " + splitEmailByAt.length);
        }
        if (splitEmailByAt[0].isEmpty() || splitEmailByAt[1].isEmpty()){
            throw new ValidationEmailException("Invalid email " + email);
        }
    }
    public void simpleSentEmail(String email, String subject, String text) throws Exception {
        this.validationEmail(email);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("davidstupenarima@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        this.sender.send(mailMessage);
    }
}
