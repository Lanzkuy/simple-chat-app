package com.lacorp.simple_chat_app.domain.usecase.validator;

public class ValidateFullname {

    public ValidationResult execute(String fullname) {
        if(fullname.isEmpty()) {
            return new ValidationResult(
                    false,
                    "Fullname must be filled"
            );
        }

        if(fullname.length() < 4) {
            return new ValidationResult(
                    false,
                    "Fullname needs to consist of at least 4 characters"
            );
        }

        return new ValidationResult(true);
    }
}
