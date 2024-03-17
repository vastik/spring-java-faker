package com.vastik.spring.data.faker.handlers;

import com.vastik.spring.data.faker.AnnotationHandler;
import com.vastik.spring.data.faker.annotation.*;

public class SimpleAnnotationHandlers {

    public static AnnotationHandler<FakeBoolean> fakeBooleanAnnotationHandler() {
        return (annotation, context) -> context.getFaker().bool().bool();
    }

    public static AnnotationHandler<FakeBothify> fakeBothifyAnnotationHandler() {
        return (annotation, context) -> context.getFaker().bothify(annotation.value(), annotation.upper());
    }

    public static AnnotationHandler<FakeLetterify> fakeLetterifyAnnotationHandler() {
        return (annotation, context) -> context.getFaker().letterify(annotation.value(), annotation.uppercase());
    }

    public static AnnotationHandler<FakeNumberify> fakeNumberifyAnnotationHandler() {
        return (annotation, context) -> context.getFaker().numerify(annotation.value());
    }

    public static AnnotationHandler<FakeRegexify> fakeRegexifyAnnotationHandler() {
        return (annotation, context) -> context.getFaker().regexify(annotation.value());
    }

    public static AnnotationHandler<FakeInclude> fakeIncludeAnnotationHandler() {
        return (annotation, context) -> context.getDataFaker().fake(context.getType());
    }
}
