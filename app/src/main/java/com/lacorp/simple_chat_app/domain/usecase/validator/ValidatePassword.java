package com.lacorp.simple_chat_app.domain.usecase.validator;

import java.util.regex.Pattern;

public class ValidatePassword {

    public ValidationResult execute(String password, boolean isLogin) {
        if(password.isEmpty()) {
            return new ValidationResult(
                    false,
                    "Password must be filled"
            );
        }

        if(!isLogin) {
            if(password.length() < 8) {
                return new ValidationResult(
                        false,
                        "Password needs to consist of at least 8 characters"
                );
            }

            if(Pattern.matches("[a-zA-Z0-9]", password)) {
                return new ValidationResult(
                        false,
                        "Password needs to contains of at least one letter and digit"
                );
            }
        }

        return new ValidationResult(true);
    }
}
