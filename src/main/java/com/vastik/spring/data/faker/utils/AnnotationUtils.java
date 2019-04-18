package com.vastik.spring.data.faker.utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public final class AnnotationUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> cl) {
        return (T) Arrays.stream(annotations)
                .filter(a -> a.annotationType().equals(cl))
                .findAny()
                .orElse(null);
    }

    public static boolean hasAnnotation(Annotation[] annotations, Class<? extends Annotation> cl) {
        return Arrays.stream(annotations).anyMatch(a -> a.annotationType().equals(cl));
    }

    public static boolean hasAnyAnnotation(Annotation[] annotations, Class<?> ... types) {
        return Arrays.stream(annotations).anyMatch(a -> Arrays.stream(types).anyMatch(b -> a.annotationType().equals(b)));
    }
}
