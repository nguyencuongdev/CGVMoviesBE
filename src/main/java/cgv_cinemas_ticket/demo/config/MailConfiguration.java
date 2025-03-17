package cgv_cinemas_ticket.demo.config;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {
    @NonFinal
    @Value("${spring.mail.host}")
    String mailHost;

    @NonFinal
    @Value("${spring.mail.username}")
    String username;

    @NonFinal
    @Value("${spring.mail.password}")
    String password;

    @NonFinal
    @Value("${spring.mail.port}")
    int port;

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        // Enable TLS/SSL
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", "true");
        mailSender.getJavaMailProperties().put("mail.smtp.auth", "true");

        return mailSender;
    }
}
