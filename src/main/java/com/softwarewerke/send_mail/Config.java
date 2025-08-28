package com.softwarewerke.send_mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class Config {

    @Bean
    // Makes application.properties and command line args shorter and readable:
    @ConfigurationProperties(prefix = "mail")
    public JavaMailSenderImpl mailSender() {
        return new JavaMailSenderImpl();
    }
}
