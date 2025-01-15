package com.applab.library_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message="email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message="password is required")
    private String password;
}
