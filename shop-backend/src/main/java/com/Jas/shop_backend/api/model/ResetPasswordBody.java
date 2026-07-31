package com.Jas.shop_backend.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordBody {

    @NotBlank(message = "Email Address is required")
    @NotNull(message = "Email Address is required")
    @Email
    private String email;

    @NotBlank(message = "Reset token is required")
    @NotNull(message = "Reset token is required")
    private String token;

    @NotNull(message = "Password required")
    @NotBlank(message = "Password required")
    @Pattern(regexp = "^(?=.*[A-Z]).{8,}$", message = "Password must be at least 8 characters and contain at least one uppercase letter")
    private String password;

    @NotNull(message = "Password Confirmation required")
    @NotBlank(message = "Password Confirmation required")
    private String password_confirmation;
}
