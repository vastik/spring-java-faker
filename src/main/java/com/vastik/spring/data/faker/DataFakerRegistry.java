package com.vastik.spring.data.faker;

import com.vastik.spring.data.faker.annotation.*;
import com.vastik.spring.data.faker.handlers.*;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class DataFakerRegistry {

    private Map<Class<? extends Annotation>, AnnotationHandler<? extends Annotation>> classMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    DataFakerRegistry() {
        registerHandler(FakeBoolean.class, SimpleAnnotationHandlers.fakeBooleanAnnotationHandler());
        registerHandler(FakeBothify.class, SimpleAnnotationHandlers.fakeBothifyAnnotationHandler());
        registerHandler(FakeLetterify.class, SimpleAnnotationHandlers.fakeLetterifyAnnotationHandler());
        registerHandler(FakeNumberify.class, SimpleAnnotationHandlers.fakeNumberifyAnnotationHandler());
        registerHandler(FakeRegexify.class, SimpleAnnotationHandlers.fakeRegexifyAnnotationHandler());
        registerHandler(FakeNumberBetween.class, NumericAnnotationHandlers.fakeNumberBetweenAnnotationHandler());
        registerHandler(FakeNumberRandom.class, NumericAnnotationHandlers.fakeNumberRandomAnnotationHandler());
        registerHandler(FakeNumberDigits.class, NumericAnnotationHandlers.fakeNumberDigitsAnnotationHandler());

        registerHandler(FakeDateBetween.class, DateAnnotationHandlers.fakeDateBetweenAnnotationHandler());
        registerHandler(FakeDateFuture.class, DateAnnotationHandlers.fakeDateFutureAnnotationHandler());
        registerHandler(FakeDatePast.class, DateAnnotationHandlers.fakeDatePastAnnotationHandler());
        registerHandler(FakeDateNow.class, DateAnnotationHandlers.fakeDateNowAnnotationHandler());

        registerHandler(FakeInclude.class, SimpleAnnotationHandlers.fakeIncludeAnnotationHandler());
        registerHandler(FakeCollection.class, new FakeCollectionAnnotationHandler());
        registerHandler(FakeEnum.class, new FakeEnumAnnotationHandler());
        registerHandler(FakeFaker.class, new FakeFakerAnnotationHandler());
    }

    public <T extends Annotation> void registerHandler(Class<T> annotationClass, AnnotationHandler<T> annotationHandler) {
        classMap.put(annotationClass, annotationHandler);
    }

    public Object handle(DataFakeContext context) throws Exception {
        FakeCollection fakeCollection = AnnotationUtils.getAnnotation(context.getAnnotations(), FakeCollection.class);
        if (fakeCollection != null)
            return invokeHandler(fakeCollection, context);

        for (Annotation annotation : context.getAnnotations()) {
            if (hasHandler(annotation))
                return invokeHandler(annotation, context);
        }

        return null;
    }

    public <T extends Annotation> boolean hasHandler(T annotation) {
        return classMap.containsKey(annotation.annotationType());
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> Object invokeHandler(T annotation, DataFakeContext context) throws Exception {
        AnnotationHandler<T> handler = (AnnotationHandler<T>) classMap.get(annotation.annotationType());
        return handler.get(annotation, context);
    }
}
