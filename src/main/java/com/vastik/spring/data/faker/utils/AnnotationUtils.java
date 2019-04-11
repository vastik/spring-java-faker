package com.vastik.spring.data.faker.utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

public final class AnnotationUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> Optional<T> getAnnotation(Annotation[] annotations, Class<T> cl) {
        return (Optional<T>) Arrays.stream(annotations)
                .filter(a -> a.annotationType().equals(cl))
                .findAny();
    }

    public static boolean hasAnnotation(Annotation[] annotations, Class<? extends Annotation> cl) {
        return Arrays.stream(annotations).anyMatch(a -> a.annotationType().equals(cl));
    }
}
