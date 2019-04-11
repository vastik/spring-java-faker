package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;
import com.vastik.spring.data.faker.annotation.FakeBetween;
import com.vastik.spring.data.faker.annotation.FakeFuture;
import com.vastik.spring.data.faker.annotation.FakePast;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Date;

public class DateTypeFaker implements DataTypeFaker<Date> {

    @Override
    public Date getValue(DataFakeContext dataFakeContext) {
        final WrappedValue<Date> value = new WrappedValue<>();
        final Faker faker = dataFakeContext.getFaker();
        final Annotation[] annotations = dataFakeContext.getAnnotations();

        AnnotationUtils.getAnnotation(annotations, FakeBetween.class).ifPresent(v -> {
            Date past = faker.date().past(v.past().value(), v.past().unit());
            Date future = faker.date().future(v.future().value(), v.future().unit());
            value.set(faker.date().between(past, future));
        });

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakePast.class)
                    .ifPresent(v -> value.set(faker.date().past(v.value(), v.unit())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeFuture.class)
                    .ifPresent(v -> value.set(faker.date().future(v.value(), v.unit())));

        return value.get();
    }
}
