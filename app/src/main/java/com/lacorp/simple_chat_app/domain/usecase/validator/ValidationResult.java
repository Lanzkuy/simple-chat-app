package com.lacorp.simple_chat_app.domain.usecase.validator;

public class ValidationResult {

    private final Boolean successful;
    private final String errorMessage;

    public ValidationResult(Boolean successful, String errorMessage) {
        this.successful = successful;
        this.errorMessage = errorMessage;
    }

    public ValidationResult(Boolean successful) {
        this(successful, null);
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
