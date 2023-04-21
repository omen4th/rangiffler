package org.rangiffler.utils;

public enum ErrorMessage {
    BAD_CREDENTIALS("Bad credentials"),
    PASSWORDS_SHOULD_BE_EQUAL("Passwords should be equal");

    public final String content;

    ErrorMessage(String content) {
        this.content = content;
    }
}
