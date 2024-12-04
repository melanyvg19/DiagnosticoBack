package com.diagnostico_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${application.mail.sender.account}")
    private String account;

    @Value("${application.mail.sender.account.password}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp-mail.outlook.com");
        mailSender.setPort(587);
        mailSender.setUsername(account);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");//Establece el protocolo
        props.put("mail.smtp.auth", "true");//Habilita la autenticacion
        props.put("mail.smtp.starttls.enable", "true");//Habilita el cifrado entre el Host y la aplicacion
        props.put("mail.debug", "true");

        return mailSender;
    }

}
