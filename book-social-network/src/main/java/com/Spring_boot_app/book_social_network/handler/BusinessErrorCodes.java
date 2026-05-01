package com.Spring_boot_app.book_social_network.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED,"No code is implemented yet"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User email is locked"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is not correct"),
    NEW_PASSWORD_NOT_MATCH(301, BAD_REQUEST, "New password does not match"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Email or password is wrong");


    private final int code;
    private final HttpStatus httpStatus;
    private final String description;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
