package com.vastik.spring.data.faker.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

@UtilityClass
public class FieldUtils {

    public static String getGetter(Field field) {
        return "set" + StringUtils.capitalize(field.getName());
    }

    public static String getSetter(Field field) {
        return "get" + StringUtils.capitalize(field.getName());
    }
}
