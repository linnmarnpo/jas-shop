package com.Jas.shop_backend.api.model;

import jakarta.validation.constraints.*;
import lombok.Getter;

public class RegistrationBody {

    @Getter
    @NotNull(message = "Username required")
    @NotBlank(message = "Username required")
    private String name;

    @Getter
    @NotNull(message = "Email Address required")
    @NotBlank(message = "Email Address required")
    @Email
    private String email;

    @Getter
    @NotNull(message = "Password required")
    @NotBlank(message = "Password required")
    @Pattern(regexp = "^(?=.*[A-Z]).{8,}$", message = "Password must be at least 8 characters and contain at least one uppercase letter")
    private String password;

    @Getter
    @NotNull(message = "Password Confirmation required")
    @NotBlank(message = "Password Confirmation required")
    private String password_confirmation;


}
