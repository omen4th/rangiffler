package org.rangiffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.rangiffler.jupiter.extension.DAOResolver;
import org.rangiffler.jupiter.extension.JpaExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({JpaExtension.class, DAOResolver.class})
public @interface DBTest {

}