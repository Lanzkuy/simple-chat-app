package com.lacorp.simple_chat_app.domain.usecase.validator;

public class ValidateFullname {

    public ValidationResult execute(String username) {
        if(username.isEmpty()) {
            return new ValidationResult(
                    false,
                    "Fullname must be filled"
            );
        }

        return new ValidationResult(true);
    }
}
