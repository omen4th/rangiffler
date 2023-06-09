package org.rangiffler.jupiter.annotation.meta;

import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rangiffler.jupiter.extension.BrowserExtension;
import org.rangiffler.jupiter.extension.ClearCookiesAndSessionExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({BrowserExtension.class, ClearCookiesAndSessionExtension.class, AllureJunit5.class})
public @interface WebTest {

}