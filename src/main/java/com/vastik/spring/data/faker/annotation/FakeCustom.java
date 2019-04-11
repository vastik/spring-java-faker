package com.vastik.spring.data.faker.annotation;

import com.vastik.spring.data.faker.DataTypeFaker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FakeCustom {
    Class<? extends DataTypeFaker> value();
}
