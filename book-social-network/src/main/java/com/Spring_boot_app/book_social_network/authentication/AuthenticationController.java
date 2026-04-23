package com.Spring_boot_app.book_social_network.authentication;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "auth")
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerNewUser(@RequestBody @Valid RegistrationRequest registrationRequest) throws MessagingException {
        authenticationService.register(registrationRequest);
        return ResponseEntity.accepted().build();
    }
}
