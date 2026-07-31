package com.Jas.shop_backend.api.controller.auth;

import com.Jas.shop_backend.Service.UserDetailService;
import com.Jas.shop_backend.api.helper.JWTTokenHelper;
import com.Jas.shop_backend.api.model.ApiResponse;
import com.Jas.shop_backend.api.model.LoginBody;
import com.Jas.shop_backend.api.model.RegistrationBody;
import com.Jas.shop_backend.api.model.UserToken;
import com.Jas.shop_backend.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailService userDetailService;
    private final JWTTokenHelper jwtTokenHelper;

    public AuthController(AuthenticationManager authenticationManager, UserDetailService userDetailService, JWTTokenHelper jwtTokenHelper) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserToken>> login(@Valid @RequestBody LoginBody loginBody) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);

        if (authenticationResponse.isAuthenticated() && authenticationResponse.getPrincipal() instanceof User user) {
            String token = jwtTokenHelper.generateToken(user.getEmail());
            UserToken userToken = UserToken.builder()
                    .token(token)
                    .build();

            ApiResponse<UserToken> response = new ApiResponse<>(
                    true,
                    "User login successful",
                    userToken
            );
            return ResponseEntity.ok(response);
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegistrationBody registrationBody) {
        if (!Objects.equals(registrationBody.getPassword(), registrationBody.getPassword_confirmation())) {
            ApiResponse<String> response = new ApiResponse<>(
                    false,
                    "Password confirmation does not match",
                    null,
                    List.of(Map.of("password", List.of("Password confirmation does not match")))
            );
            return ResponseEntity.badRequest().body(response);
        }

        userDetailService.createUser(registrationBody);
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Registration successful. Please verify your email.",
                "USER_REGISTERED"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyCode(@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String code = map.get("code");

        User user = (User) userDetailService.loadUserByUsername(userName);
        if (user == null || user.getVerificationCode() == null || !user.getVerificationCode().equals(code)) {
            ApiResponse<String> response = new ApiResponse<>(
                    false,
                    "Invalid verification code",
                    null
            );
            return ResponseEntity.badRequest().body(response);
        }

        userDetailService.verifyUser(userName);
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Email verified successfully",
                "VERIFIED"
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping({"/forgot-password", "/auth/forgot-password"})
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody com.Jas.shop_backend.api.model.ForgotPasswordBody forgotPasswordBody) {
        userDetailService.processForgotPassword(forgotPasswordBody.getEmail());
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Password reset email sent successfully. Please check your inbox.",
                "RESET_EMAIL_SENT"
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping({"/reset-password", "/auth/reset-password"})
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody com.Jas.shop_backend.api.model.ResetPasswordBody resetPasswordBody) {
        userDetailService.processResetPassword(resetPasswordBody);
        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Password reset successfully. Please log in.",
                "PASSWORD_RESET_SUCCESS"
        );
        return ResponseEntity.ok(response);
    }
}
