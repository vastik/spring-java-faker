package com.vastik.spring.data.faker;

import java.lang.annotation.Annotation;

public interface AnnotationHandler<T extends Annotation> {
    Object get(T annotation, DataFakeContext context) throws Exception;
}
