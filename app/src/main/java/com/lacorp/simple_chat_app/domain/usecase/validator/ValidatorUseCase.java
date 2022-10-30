package com.lacorp.simple_chat_app.domain.usecase.validator;

public class ValidatorUseCase {

    private final ValidateUsername validateUsername;
    private final ValidatePassword validatePassword;
    private final ValidateFullname validateFullname;

    public ValidatorUseCase(
            ValidateUsername validateUsername,
            ValidatePassword validatePassword,
            ValidateFullname validateFullname
    ) {
        this.validateUsername = validateUsername;
        this.validatePassword = validatePassword;
        this.validateFullname = validateFullname;
    }

    public ValidateUsername getValidateUsername() {
        return validateUsername;
    }

    public ValidatePassword getValidatePassword() {
        return validatePassword;
    }

    public ValidateFullname getValidateFullname() {
        return validateFullname;
    }
}
