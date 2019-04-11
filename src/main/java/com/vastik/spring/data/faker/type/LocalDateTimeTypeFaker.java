package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;
import com.vastik.spring.data.faker.annotation.FakeBetween;
import com.vastik.spring.data.faker.annotation.FakeFuture;
import com.vastik.spring.data.faker.annotation.FakeNow;
import com.vastik.spring.data.faker.annotation.FakePast;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeTypeFaker implements DataTypeFaker<LocalDateTime> {

    @Override
    public LocalDateTime getValue(DataFakeContext dataFakeContext) {
        final WrappedValue<LocalDateTime> value = new WrappedValue<>();

        final Faker faker = dataFakeContext.getFaker();
        final Annotation[] annotations = dataFakeContext.getAnnotations();

        AnnotationUtils.getAnnotation(annotations, FakeBetween.class).ifPresent(v -> {
            Date past = faker.date().past(v.past().value(), v.past().unit());
            Date future = faker.date().future(v.future().value(), v.future().unit());
            value.set(convert(faker.date().between(past, future)));
        });

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakePast.class)
                    .ifPresent(v -> value.set(convert(faker.date().past(v.value(), v.unit()))));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeFuture.class)
                    .ifPresent(v -> value.set(convert(faker.date().future(v.value(), v.unit()))));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeNow.class)
                    .ifPresent(v -> value.set(LocalDateTime.now()));

        return value.get();
    }

    private LocalDateTime convert(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
