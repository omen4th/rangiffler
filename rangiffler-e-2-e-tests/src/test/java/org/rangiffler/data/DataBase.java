package org.rangiffler.data;

import org.apache.commons.lang3.StringUtils;
import org.rangiffler.config.Config;

public enum DataBase {
    USERDATA("jdbc:postgresql://%s/rangiffler-auth"),
    AUTH("jdbc:postgresql://%s/rangiffler-geo"),
    SPEND("jdbc:postgresql://%s/rangiffler-photo"),
    CURRENCY("jdbc:postgresql://%s/rangiffler-users");
    private final String url;

    DataBase(String url) {
        this.url = url;
    }

    private static final Config CFG = Config.getConfig();

    public String getUrl() {
        return String.format(url, CFG.databaseAddress());
    }

    public String getUrlForP6Spy() {
        return "jdbc:p6spy:" + StringUtils.substringAfter(getUrl(), "jdbc:");
    }
}
