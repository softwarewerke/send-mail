package com.softwarewerke.send_mail;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class SendMailApplication implements CommandLineRunner {

    private final SendMailCommand sendMailCommand;

    public SendMailApplication(SendMailCommand sendMailCommand) {
        this.sendMailCommand = sendMailCommand;
    }

    public static void main(String[] args) {
        SpringApplication.run(SendMailApplication.class, args);
    }

    @Override
    public void run(String... args) {
        int exitCode = new CommandLine(sendMailCommand).execute(args);
        System.exit(exitCode);
    }
}
