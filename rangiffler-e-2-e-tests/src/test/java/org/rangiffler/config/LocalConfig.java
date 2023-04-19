package org.rangiffler.config;

import com.codeborne.selenide.Configuration;

public class LocalConfig implements Config {

    static {
        Configuration.browserSize = "1980x1024";
    }

    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3001/";
    }

    @Override
    public String gatewayUrl() {
        return "http://127.0.0.1:8080/";
    }

    public String databaseAddress() {
        return "127.0.0.1:5432";
    }
}
