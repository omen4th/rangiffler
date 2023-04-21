package org.rangiffler.jupiter.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rangiffler.api.context.CookieHolder;
import org.rangiffler.api.context.SessionStorageHolder;

public class ClearCookiesAndSessionExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CookieHolder.getInstance().flushAll();
        SessionStorageHolder.getInstance().flushAll();
    }
}
