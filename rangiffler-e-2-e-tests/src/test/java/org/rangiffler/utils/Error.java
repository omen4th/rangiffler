package org.rangiffler.utils;

public enum Error {
    BAD_CREDENTIALS("Bad credentials");

    public final String content;

    Error(String content) {
        this.content = content;
    }
}
