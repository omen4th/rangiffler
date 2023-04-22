package org.rangiffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.rangiffler.jupiter.extension.ClearCookiesAndSessionExtension;
import org.rangiffler.jupiter.extension.CreateUserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@ExtendWith({CreateUserExtension.class, ClearCookiesAndSessionExtension.class})
public @interface GenerateUser {

    boolean handleAnnotation() default true;

    String username() default "";

    String firstname() default "";

    String lastname() default "";

    String avatarPath() default "";

    String password() default "";

    Friends friends() default @Friends(handleAnnotation = false);

    IncomeInvitations incomeInvitations() default @IncomeInvitations(handleAnnotation = false);

    OutcomeInvitations outcomeInvitations() default @OutcomeInvitations(handleAnnotation = false);
}
