package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final HttpClient httpClient;

    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Value("${spring.mail.username:linnmarn882@gmail.com}")
    private String sender;

    @Value("${resend.api.key:${RESEND_API_KEY:}}")
    private String resendApiKey;

    @Value("${brevo.api.key:${BREVO_API_KEY:}}")
    private String brevoApiKey;

    public void sendMail(User user) throws Exception {
        Context context = new Context();
        context.setVariable("name", user.getName());
        context.setVariable("code", user.getVerificationCode());

        String htmlContent = templateEngine.process("verification_email", context);

        sendEmailViaAvailableProvider(user.getEmail(), "Verify Your Email", htmlContent);
    }

    public void sendResetPasswordMail(User user, String token) throws Exception {
        Context context = new Context();
        context.setVariable("name", user.getName());
        String resetUrl = "http://localhost:5173/password-reset/" + token + "?email=" + user.getEmail();
        context.setVariable("resetUrl", resetUrl);

        String htmlContent = templateEngine.process("reset_password_email", context);

        sendEmailViaAvailableProvider(user.getEmail(), "Reset Your Password - Jas Shop", htmlContent);
    }

    private void sendEmailViaAvailableProvider(String recipientEmail, String subject, String htmlContent) throws Exception {
        if (resendApiKey != null && !resendApiKey.isBlank()) {
            sendViaResend(recipientEmail, subject, htmlContent);
            return;
        }

        if (brevoApiKey != null && !brevoApiKey.isBlank()) {
            sendViaBrevo(recipientEmail, subject, htmlContent);
            return;
        }

        sendViaSmtp(recipientEmail, subject, htmlContent);
    }

    private void sendViaResend(String recipientEmail, String subject, String htmlContent) throws Exception {
        String escapeJsonHtml = htmlContent.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        String jsonPayload = String.format("""
            {
              "from": "Jas Shop <onboarding@resend.dev>",
              "to": ["%s"],
              "subject": "%s",
              "html": "%s"
            }
            """, recipientEmail, subject, escapeJsonHtml);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.resend.com/emails"))
                .header("Authorization", "Bearer " + resendApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            log.info("Email sent successfully via Resend API to {}", recipientEmail);
        } else {
            log.error("Resend API failed to send email to {}. Response: {}", recipientEmail, response.body());
            throw new RuntimeException("Resend API error: " + response.body());
        }
    }

    private void sendViaBrevo(String recipientEmail, String subject, String htmlContent) throws Exception {
        String escapeJsonHtml = htmlContent.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        String jsonPayload = String.format("""
            {
              "sender": {"name": "Jas Shop", "email": "%s"},
              "to": [{"email": "%s"}],
              "subject": "%s",
              "htmlContent": "%s"
            }
            """, sender, recipientEmail, subject, escapeJsonHtml);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("api-key", brevoApiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            log.info("Email sent successfully via Brevo API to {}", recipientEmail);
        } else {
            log.error("Brevo API failed to send email to {}. Response: {}", recipientEmail, response.body());
            throw new RuntimeException("Brevo API error: " + response.body());
        }
    }

    private void sendViaSmtp(String recipientEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setFrom(sender);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        javaMailSender.send(message);
        log.info("Email sent successfully via SMTP to {}", recipientEmail);
    }
}
