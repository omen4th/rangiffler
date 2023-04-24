package org.rangiffler.jupiter.annotation;

import org.rangiffler.jupiter.extension.CreateUserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {
    CreateUserExtension.USE use() default CreateUserExtension.USE.LOGIN;
}
