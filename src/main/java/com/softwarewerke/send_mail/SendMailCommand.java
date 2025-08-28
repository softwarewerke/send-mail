package com.softwarewerke.send_mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

@Component
@Command(name = "java -jar send-mail.jar", description = "Send a mail from command line")
public class SendMailCommand implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Autowired
    private JavaMailSender mailSender;

    @CommandLine.Option(names = {"-C"}, description = "Print example configuration and exit")
    private boolean printConfig;

    @CommandLine.Option(names = {"-s", "--subject"}, defaultValue = "", paramLabel = "subject", description = "Email subject")
    private String subjectArg;

    @CommandLine.Option(names = {"-m", "--message"}, defaultValue = "", paramLabel = "message", description = "The message, overrides stdin")
    private String messageArg;

    @CommandLine.Option(names = {"-f", "--from"}, defaultValue = "", paramLabel = "from", description = "From address")
    private String fromArg;

    @CommandLine.Option(names = {"-t", "--to"}, defaultValue = "", paramLabel = "to", description = "To address separated by ';'")
    private String toArg;

    @CommandLine.Option(names = {"-c", "--cc"}, defaultValue = "", paramLabel = "cc", description = "Cc addresses separated by ';'")
    private String ccArg;

    @CommandLine.Option(names = {"-b", "--bcc"}, defaultValue = "", paramLabel = "bcc", description = "Bcc addresses separated by ';'")
    private String bccArg;

    @CommandLine.Option(names = {"-H"}, description = "Send as HTML mail")
    private boolean htmlArg;

    @Override
    public void run() {

        if (spec.commandLine().getParseResult().originalArgs().isEmpty()) {
            spec.commandLine().usage(System.out);
            return;
        }

        if (printConfig) {
            printConfig();
            return;
        }

        String stdinText = readStdin();
        sendMail(stdinText);
    }

    private String readStdin() {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private void printConfig() {
        URL url = getClass().getClassLoader().getResource("application.properties");
        try (
                InputStream in = getClass()
                        .getClassLoader()
                        .getResourceAsStream("application.properties");
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            boolean start = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("# config-end")) {
                    break;
                }
                if (start) {
                    System.out.println(line);
                }
                start = start || line.startsWith("# config-start");
            }
        } catch (IOException e) {
            System.err.println("Failed to read application.properties");
        }
    }

    private void sendMail(String stdinText) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo("to");
        message.setSubject("subject");
        message.setText(isBlank(stdinText) ? messageArg : stdinText);

        mailSender.send(message);
    }

    private boolean isBlank(String stdinText) {
        return stdinText == null || stdinText.trim().isEmpty();
    }
}
