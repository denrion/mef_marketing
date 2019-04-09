package com.github.denrion.mef_marketing.config;

public class DuplicateUsernameException extends RuntimeException {

    private String message;

    public DuplicateUsernameException(String message) {
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