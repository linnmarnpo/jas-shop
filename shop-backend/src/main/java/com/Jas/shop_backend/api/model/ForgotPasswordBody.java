package com.Jas.shop_backend.api.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordBody {

    @NotBlank(message = "Email Address is required")
    @NotNull(message = "Email Address is required")
    @Email(message = "Invalid email format")
    private String email;
}
