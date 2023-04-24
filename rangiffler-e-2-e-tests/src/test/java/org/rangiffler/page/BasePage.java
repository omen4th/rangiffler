package org.rangiffler.page;

import org.rangiffler.config.Config;

public abstract class BasePage<T extends BasePage> {
    protected static final Config CFG = Config.getConfig();

    public abstract T waitForPageLoaded();

}
