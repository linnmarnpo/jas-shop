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

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(User user) throws MessagingException {
        try {
            Context context = new Context();
            context.setVariable("name", user.getName());
            context.setVariable("code", user.getVerificationCode());

            String htmlContent = templateEngine.process("verification_email", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setFrom(sender);
            helper.setSubject("Verify Your Email");
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
            log.info("Verification email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    public void sendResetPasswordMail(User user, String token) throws MessagingException {
        try {
            Context context = new Context();
            context.setVariable("name", user.getName());
            String resetUrl = "http://localhost:5173/password-reset/" + token + "?email=" + user.getEmail();
            context.setVariable("resetUrl", resetUrl);

            String htmlContent = templateEngine.process("reset_password_email", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(user.getEmail());
            helper.setFrom(sender);
            helper.setSubject("Reset Your Password - Jas Shop");
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
            log.info("Password reset email sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", user.getEmail(), e.getMessage(), e);
            throw e;
        }
    }
}
