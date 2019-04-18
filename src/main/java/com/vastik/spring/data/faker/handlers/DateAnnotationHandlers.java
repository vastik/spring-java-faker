package com.vastik.spring.data.faker.handlers;

import com.vastik.spring.data.faker.AnnotationHandler;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.annotation.FakeDateBetween;
import com.vastik.spring.data.faker.annotation.FakeDateFuture;
import com.vastik.spring.data.faker.annotation.FakeDateNow;
import com.vastik.spring.data.faker.annotation.FakeDatePast;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateAnnotationHandlers {
    public static AnnotationHandler<FakeDateBetween> fakeDateBetweenAnnotationHandler() {
        return (annotation, context) -> {
            Date past = context.getFaker().date().past(annotation.past().value(), annotation.past().unit());
            Date future = context.getFaker().date().future(annotation.future().value(), annotation.future().unit());
            return context.getFaker().date().between(past, future);
        };
    }

    public static AnnotationHandler<FakeDateFuture> fakeDateFutureAnnotationHandler() {
        return (annotation, context) -> cast(context.getFaker().date().future(annotation.value(), annotation.unit()), context);
    }

    public static AnnotationHandler<FakeDatePast> fakeDatePastAnnotationHandler() {
        return (annotation, context) -> cast(context.getFaker().date().past(annotation.value(), annotation.unit()), context);
    }

    public static AnnotationHandler<FakeDateNow> fakeDateNowAnnotationHandler() {
        return (annotation, context) -> cast(LocalDateTime.now(), context);
    }

    private static Object cast(Date value, DataFakeContext context) {
        if (context.getType().equals(LocalDateTime.class))
            return LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());

        return value;
    }

    private static Object cast(LocalDateTime value, DataFakeContext context) {
        if (context.getType().equals(Date.class))
            return Date.from(value.atZone(ZoneId.systemDefault()).toInstant());

        return value;
    }
}
