package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;
import com.vastik.spring.data.faker.annotation.FakeBothify;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeLetterify;
import com.vastik.spring.data.faker.annotation.FakeNumberify;
import com.vastik.spring.data.faker.utils.AnnotationUtils;
import com.vastik.spring.data.faker.utils.FakerUtils;

import java.lang.annotation.Annotation;

public class StringTypeFaker implements DataTypeFaker<String> {


    @Override
    public String getValue(DataFakeContext dataFakeContext) {
        final WrappedValue<String> value = new WrappedValue<>();
        final Faker faker = dataFakeContext.getFaker();
        final Annotation[] annotations = dataFakeContext.getAnnotations();

        AnnotationUtils.getAnnotation(annotations, FakeFaker.class)
                .ifPresent(v -> value.set(FakerUtils.getFakerValue(faker, v.value(), String.class)));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeLetterify.class)
                    .ifPresent(v -> value.set(faker.letterify(v.value(), v.uppercase())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeBothify.class)
                    .ifPresent(v -> value.set(faker.bothify(v.value(), v.uppercase())));

        if (value.unset())
            AnnotationUtils.getAnnotation(annotations, FakeNumberify.class)
                    .ifPresent(v -> value.set(faker.numerify(v.value())));

        if (value.unset())
            value.set(faker.bothify("?#?#?#?#", true));

        return value.get();
    }
}
