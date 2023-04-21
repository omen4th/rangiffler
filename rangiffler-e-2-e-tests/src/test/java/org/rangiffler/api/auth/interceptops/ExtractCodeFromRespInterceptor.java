package org.rangiffler.api.auth.interceptops;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.rangiffler.api.auth.context.SessionStorageHolder;

import java.io.IOException;

public class ExtractCodeFromRespInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String code = chain.request().url().queryParameter("code");
        if (code != null) {
            SessionStorageHolder.getInstance().addCode(code);
        }
        return chain.proceed(chain.request());
    }
}
