package com.Spring_boot_app.book_social_network.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Service;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotEmpty(message = "The email is mandatory")
    @NotBlank(message = "The email is mandatory")
    @Email(message = "Email is not formatted")
    private String email;
    @NotEmpty(message = "The password is mandatory")
    @NotBlank(message = "The password is mandatory")
    @Size(min = 8, message = "Password must be 8 characters or more")
    private String password;
}
