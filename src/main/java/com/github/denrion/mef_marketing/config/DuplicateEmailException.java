package com.github.denrion.mef_marketing.config;

public class DuplicateEmailException extends RuntimeException {

    private String message;

    public DuplicateEmailException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
