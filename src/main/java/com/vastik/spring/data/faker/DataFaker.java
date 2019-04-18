package com.vastik.spring.data.faker;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.beans.Expression;
import java.beans.Statement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

@Component
public class DataFaker {
    private final DataFakerRegistry registry = new DataFakerRegistry();
    private final Faker faker = new Faker();
    private final static Logger log = LogManager.getLogger(DataFaker.class);

    public DataFaker() {
    }

    public <T> T fake(Class<? extends T> targetClass) throws Exception {
        T t = targetClass.newInstance();
        fake(targetClass, t);
        return t;
    }

    public <T> T fake(T instance) throws Exception {
        fake(instance.getClass(), instance);
        return instance;
    }

    private void fake(Class<?> cl, Object instance) throws Exception {
        for (Field field : cl.getDeclaredFields())
            setValue(instance, field);
    }

    private void setValue(Object o, Field field) throws Exception {
        if (!field.isAccessible() && !hasSetter(o, field))
            return;

        ParameterizedType parameterizedType = null;
        if (field.getGenericType() instanceof ParameterizedType)
            parameterizedType = (ParameterizedType)field.getGenericType();

        DataFakeContext context = new DataFakeContext(this, faker, field.getAnnotations(), field.getType(), parameterizedType);
        Object object = registry.handle(context);

        if (object == null)
            return;

        if (field.isAccessible())
            field.set(o, object);
        else {
            String setterName = "set" + StringUtils.capitalize(field.getName());
            Statement statement = new Statement(o, setterName, new Object[]{object});
            statement.execute();
        }
    }

    private boolean canOverride(Object o, Field field) throws Exception {
        if (field.isAccessible()) {
            return field.get(o) != null;
        }

        if (!hasGetter(o, field)) {
            log.warn("Field {} is inaccessible and missing public getter but annotated with @FakeOverride", field.getName());
            return true;
        }

        Expression expression = new Expression(o, getGetter(field), new Object[0]);
        expression.execute();
        return expression.getValue() != null;
    }

    private boolean hasGetter(Object o, Field field) {
        return Arrays.stream(o.getClass().getMethods())
                .anyMatch(method -> method.getName().equals(getGetter(field)));
    }

    private boolean hasSetter(Object o, Field field) {
        return Arrays.stream(o.getClass().getMethods())
                .anyMatch(method -> method.getName().equals(getSetter(field)));
    }

    public DataFakerRegistry getRegistry() {
        return registry;
    }

    private static String getGetter(Field field) {
        return "set" + StringUtils.capitalize(field.getName());
    }

    private static String getSetter(Field field) {
        return "get" + StringUtils.capitalize(field.getName());
    }
}
