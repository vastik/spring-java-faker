package org.communis.spring.entity.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FakePast {
    int value() default 1;
    TimeUnit unit() default TimeUnit.DAYS;
}
