package com.Spring_boot_app.book_social_network.authentication;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "The first name is mandatory")
    @NotBlank(message = "The first name is mandatory")
    private String firstName;
    @NotEmpty(message = "The last name is mandatory")
    @NotBlank(message = "The last name is mandatory")
    private String lastName;
    @NotEmpty(message = "The email is mandatory")
    @NotBlank(message = "The email is mandatory")
    @Email(message = "Email is not formatted")
    private String email;
    @NotEmpty(message = "The password is mandatory")
    @NotBlank(message = "The password is mandatory")
    @Min(value = 8, message = "Password must be 8 characters or more")
    private String password;
}
