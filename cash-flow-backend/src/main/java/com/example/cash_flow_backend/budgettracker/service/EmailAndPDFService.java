package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.exception.ValidationEmailException;
import com.example.cash_flow_backend.budgettracker.model.dto.GetCateTranDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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
            throw new ValidationEmailException("Only one at symbol is allowed. You have " + (splitEmailByAt.length - 1));
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

    public void pdfDocument(HttpServletResponse response, List<GetCateTranDTO> dataForPdf) throws IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15);
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28);
        fontTitle.setColor(128,0,128);
        Paragraph title = new Paragraph("""
                Expense overview
                
                """, fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        for (GetCateTranDTO data : dataForPdf){
            String res = data.date() + ": " + data.category() + " -> " + data.description() + ", " + data.amount() + "Kc.";
            Paragraph paragraph = new Paragraph(res, font);
            document.add(paragraph);
        }
        document.close();
    }
}
