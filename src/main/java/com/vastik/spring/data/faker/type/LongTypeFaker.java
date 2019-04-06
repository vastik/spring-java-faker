package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeNumberBetween;
import com.vastik.spring.data.faker.annotation.FakeRandom;
import com.vastik.spring.data.faker.annotation.FakeRandomNumber;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LongTypeFaker extends DataTypeFaker<Long> {

    public LongTypeFaker(Faker faker) {
        super(faker);
    }

    @Override
    public Long getValue(Field field) {
        return getValue(field.getAnnotations());
    }

    @Override
    public Long getValue(Method method) {
        return getValue(method.getAnnotations());
    }

    public Long getValue(Annotation[] annotations) {
        final WrappedValue<Long> value = new WrappedValue<>();

        AnnotationUtils.getAnnotation(annotations, FakeFaker.class)
                .ifPresent(v -> value.set(getFakerValue(v.value(), Long.class)));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeRandom.class)
                    .ifPresent(v -> value.set(faker.random().nextLong(v.value())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeNumberBetween.class)
                    .ifPresent(v -> value.set(faker.number().numberBetween(v.min(), v.max())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeRandomNumber.class)
                    .ifPresent(v -> value.set(faker.number().randomNumber(v.digits(), true)));

        if (value.unset())
            value.set(faker.random().nextLong());

        return value.get();
    }
}
