package com.diagnostico_service.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
public class MailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${application.mail.sender.account}")
    private String account;

    void sendEmail(String toUser, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(account);
        message.setTo(toUser);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    void sendEmailWithFile(String toUser, String subject, String body, File file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, StandardCharsets.UTF_8.name());

            helper.setFrom(account);
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(file.getName(), file);

            mailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    void sendEmailWithImagesAndPdf(String toUser, String subject, String body, File headerImage, File footerImage, File pdfFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(account);
            helper.setTo(toUser);
            helper.setSubject(subject);

            // Aquí defines el cuerpo del correo con imágenes referenciadas por cid (content id)
            String htmlMsg = "<html>" +
                    "<body>" +
                    "<img src='cid:headerImage' style='width:100%; height:auto;'/>" +
                    "<p>" + body + "</p>" +
                    "<img src='cid:footerImage' style='width:100%; height:auto;'/>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true);

            // Adjunta las imágenes al correo con un ID único (cid)
            FileSystemResource headerRes = new FileSystemResource(headerImage);
            helper.addInline("headerImage", headerRes);

            FileSystemResource footerRes = new FileSystemResource(footerImage);
            helper.addInline("footerImage", footerRes);

            // Adjunta el archivo PDF
            FileSystemResource pdfRes = new FileSystemResource(pdfFile);
            helper.addAttachment(pdfFile.getName(), pdfRes);

            // Envía el mensaje
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
