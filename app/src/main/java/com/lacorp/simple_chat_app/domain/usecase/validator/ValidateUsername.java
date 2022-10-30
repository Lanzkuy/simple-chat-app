package com.lacorp.simple_chat_app.domain.usecase.validator;

import java.util.regex.Pattern;

public class ValidateUsername {

    public ValidationResult execute(String username, boolean isLogin) {
        if(username.isEmpty()) {
            return new ValidationResult(
                    false,
                    "Username must be filled"
            );
        }

        if(!isLogin) {
            if(username.length() < 4) {
                return new ValidationResult(
                        false,
                        "Username needs to consist of at least 4 characters"
                );
            }
        }

        return new ValidationResult(true);
    }
}
