package com.example.emailservice.service.email;

import com.example.emailservice.jacoco.ExcludeFromJacocoGeneratedReport;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService {
    private final JavaMailSender emailSender;
    @Value("${email}")
    public String email;

    @Value("${sendgrid-key}")
    String sendGridApiKey;

    @ExcludeFromJacocoGeneratedReport
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(email);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        emailSender.send(message);
//
       try {
           Email from = new Email(email);
           Email toEmail = new Email(to);


           Content content = new Content("text/plain", text);

           Mail mail = new Mail(from, subject, toEmail, content);

           SendGrid sg = new SendGrid(sendGridApiKey);
           Request request = new Request();
           request.setMethod(Method.POST);
           request.setEndpoint("mail/send");
           request.setBody(mail.build());

           Response response = sg.api(request);

       } catch (Exception e) {
           e.printStackTrace();
       }
    }
}
