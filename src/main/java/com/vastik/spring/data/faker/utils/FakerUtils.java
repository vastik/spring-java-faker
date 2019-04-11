package com.vastik.spring.data.faker.utils;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;

public final class FakerUtils {

    @SuppressWarnings("unchecked")
    public static  <T> T getFakerValue(Faker faker, String value, Class<T> cl) {
        String[] methods = StringUtils.split(value, '.');
        Object result = faker;
        Class<?> currentClass = result.getClass();
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
