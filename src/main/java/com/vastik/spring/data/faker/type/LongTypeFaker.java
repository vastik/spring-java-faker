package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeNumberBetween;
import com.vastik.spring.data.faker.annotation.FakeRandom;
import com.vastik.spring.data.faker.annotation.FakeRandomNumber;
import com.vastik.spring.data.faker.utils.AnnotationUtils;
import com.vastik.spring.data.faker.utils.FakerUtils;

import java.lang.annotation.Annotation;

public class LongTypeFaker implements DataTypeFaker<Long> {

    @Override
    public Long getValue(DataFakeContext dataFakeContext) {
        final WrappedValue<Long> value = new WrappedValue<>();
        final Faker faker = dataFakeContext.getFaker();
        final Annotation[] annotations = dataFakeContext.getAnnotations();

        AnnotationUtils.getAnnotation(annotations, FakeFaker.class)
                .ifPresent(v -> value.set(FakerUtils.getFakerValue(faker, v.value(), Long.class)));

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
