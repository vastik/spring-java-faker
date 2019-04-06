package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

public abstract class DataTypeFaker<T> {
    protected final Faker faker;

    public DataTypeFaker(Faker faker) {
        this.faker = faker;
    }

    public abstract T getValue(Annotation[] annotations);

    @SuppressWarnings("unchecked")
    protected <T> T getFakerValue(String value, Class<T> cl) {
        String[] methods = StringUtils.split(value, '.');
        Object result = faker;
        Class<?> currentClass = faker.getClass();
        for (String method : methods) {
            try {
                result = currentClass.getMethod(method).invoke(result);
                currentClass = result.getClass();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to call faker class", ex);
            }
        }

        if (!currentClass.isAssignableFrom(cl)) {
            String msg = String.format("Faker value mismatch, required: %s, got %s", cl.getName(), currentClass.getName());
            throw new IllegalArgumentException(msg);
        }

        return (T) result;
    }
}
