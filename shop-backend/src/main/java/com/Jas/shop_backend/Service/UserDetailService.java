package com.Jas.shop_backend.Service;

import com.Jas.shop_backend.api.helper.VerificationCodeGenerator;
import com.Jas.shop_backend.api.model.RegistrationBody;
import com.Jas.shop_backend.api.model.ResetPasswordBody;
import com.Jas.shop_backend.enums.Role;
import com.Jas.shop_backend.exception.ApiConflictException;
import com.Jas.shop_backend.model.Authority;
import com.Jas.shop_backend.model.DAO.AuthorityDAO;
import com.Jas.shop_backend.model.DAO.UserDAO;
import com.Jas.shop_backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserDetailService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailService.class);

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityDAO authorityDAO;
    private final EmailService emailService;

    public UserDetailService(UserDAO userDAO, PasswordEncoder passwordEncoder, AuthorityDAO authorityDAO, EmailService emailService) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.authorityDAO = authorityDAO;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByEmail(username);
        if(null == user) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return user;
    }

    public void createUser(RegistrationBody registrationBody) {

        User existing = userDAO.findByEmail(registrationBody.getEmail());

        if(userDAO.existsByName(registrationBody.getName()))
        {
            throw new ApiConflictException("Username already exists");
        }

        if(existing != null) {
            throw new ApiConflictException("Email already exists");
        }

        try {

            User user = new User();
            user.setName(registrationBody.getName());
            user.setEmail(registrationBody.getEmail());
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(registrationBody.getPassword()));

            String code = VerificationCodeGenerator.generateCode();
            user.setVerificationCode(code);

            Authority userRole = authorityDAO.findByRole(Role.USER);

            if (userRole == null) {
                throw new BadCredentialsException("ROLE_USER not found in database");
            }
            user.setAuthorities(List.of(userRole));
            userDAO.save(user);

            //Call the method to send email
            try {
                emailService.sendMail(user);
            } catch (Exception mailException) {
                log.error("User created successfully, but verification email failed to send: {}", mailException.getMessage());
            }

        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    public void verifyUser(String userName) {
        User user = userDAO.findByEmail(userName);
        if (user != null) {
            user.setEnabled(true);
            user.setVerificationCode(null);
            userDAO.save(user);
        }
    }

    public void processForgotPassword(String email) {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new BadCredentialsException("No user found with email: " + email);
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userDAO.save(user);

        try {
            emailService.sendResetPasswordMail(user, token);
        } catch (Exception e) {
            log.error("Error sending reset password email to {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to send reset password email: " + e.getMessage(), e);
        }
    }

    public void processResetPassword(ResetPasswordBody resetPasswordBody) {
        if (!Objects.equals(resetPasswordBody.getPassword(), resetPasswordBody.getPassword_confirmation())) {
            throw new BadCredentialsException("Password confirmation does not match");
        }

        User user = userDAO.findByEmailAndResetPasswordToken(resetPasswordBody.getEmail(), resetPasswordBody.getToken());
        if (user == null) {
            throw new BadCredentialsException("Invalid or expired reset token");
        }

        if (user.getResetPasswordTokenExpiry() == null || user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Reset token has expired. Please request a new password reset link.");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordBody.getPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userDAO.save(user);
    }
}
