package org.rangiffler.jupiter.annotation;

import org.rangiffler.model.Country;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.rangiffler.model.Country.FIJI;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GeneratePhoto {
    boolean handleAnnotation() default true;

    String photoPath();

    Country country() default FIJI;

    String description() default "";

}
