package com.vastik.spring.data.faker.handlers;

import com.vastik.spring.data.faker.AnnotationHandler;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.annotation.FakeNumberBetween;
import com.vastik.spring.data.faker.annotation.FakeNumberDigits;
import com.vastik.spring.data.faker.annotation.FakeNumberRandom;

public class NumericAnnotationHandlers {
    public static AnnotationHandler<FakeNumberBetween> fakeNumberBetweenAnnotationHandler() {
        return (annotation, context) -> cast(context.getFaker().number().numberBetween(annotation.min(), annotation.max()), context);
    }

    public static AnnotationHandler<FakeNumberDigits> fakeNumberDigitsAnnotationHandler() {
        return (annotation, context) -> cast(context.getFaker().number().randomNumber(annotation.value(), false), context);
    }

    public static AnnotationHandler<FakeNumberRandom> fakeNumberRandomAnnotationHandler() {
        return (annotation, context) -> cast(context.getFaker().random().nextLong(annotation.value()), context);
    }

    private static Object cast(Long number, DataFakeContext context) {
        if (context.getType().equals(Integer.class) || context.getType().equals(int.class))
            return number.intValue();

        if (context.getType().equals(Short.class) || context.getType().equals(short.class))
            return number.shortValue();

        if (context.getType().equals(Float.class) || context.getType().equals(float.class))
            return number.floatValue();

        if (context.getType().equals(Double.class) || context.getType().equals(double.class))
            return number.doubleValue();

        if (context.getType().equals(Byte.class) || context.getType().equals(byte.class))
            return number.byteValue();

        return number;
    }
}
