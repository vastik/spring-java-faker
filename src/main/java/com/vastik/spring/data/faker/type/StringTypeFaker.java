package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.annotation.FakeBothify;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeLetterify;
import com.vastik.spring.data.faker.annotation.FakeNumberify;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StringTypeFaker extends DataTypeFaker<String> {

    public StringTypeFaker(Faker faker) {
        super(faker);
    }

    @Override
    public String getValue(Field field) {
        return getValue(field.getAnnotations());
    }

    @Override
    public String getValue(Method method) {
        return getValue(method.getAnnotations());
    }

    private String getValue(Annotation[] annotations) {
        final WrappedValue<String> value = new WrappedValue<>();

        AnnotationUtils.getAnnotation(annotations, FakeFaker.class)
                .ifPresent(v -> value.set(getFakerValue(v.value(), String.class)));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeLetterify.class)
                    .ifPresent(v -> value.set(faker.letterify(v.value())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeBothify.class)
                    .ifPresent(v -> value.set(faker.bothify(v.value())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeNumberify.class)
                    .ifPresent(v -> value.set(faker.numerify(v.value())));

        if (value.unset())
            value.set(faker.bothify("?#?#?#?#"));

        return value.get();
    }
}
