package org.rangiffler.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Config {

    Logger LOG = LoggerFactory.getLogger(Config.class);
    String PROJECT_NAME = "rangiffler";

    static Config getConfig() {
        return new LocalConfig();
    }

    String frontUrl();

    String authUrl();

    String gatewayUrl();

    String usersGrpcAddress();

    int usersGrpcPort();

    String photoGrpcAddress();

    int photoGrpcPort();

    String databaseAddress();
}
