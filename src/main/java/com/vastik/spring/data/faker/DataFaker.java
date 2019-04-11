package com.vastik.spring.data.faker;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.annotation.FakeIgnore;
import com.vastik.spring.data.faker.annotation.FakeOverride;
import com.vastik.spring.data.faker.utils.FieldUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.Expression;
import java.beans.Statement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

@Component
@Log4j2
public class DataFaker {
    private final DataFakerTypeFactory factory;
    private final DataFakerValueProvider valueProvider;
    private final Faker faker = new Faker();

    public DataFaker(DataFakerTypeFactory factory) {
        this.factory = factory;
        this.valueProvider = new DataFakerValueProvider(factory, this);
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

        if (field.isAnnotationPresent(FakeIgnore.class))
            return;

        if (field.isAnnotationPresent(FakeOverride.class) && !canOverride(o, field))
            return;

        ParameterizedType parameterizedType = null;
        if (field.getGenericType() instanceof ParameterizedType)
            parameterizedType = (ParameterizedType)field.getGenericType();

        DataFakeContext context = new DataFakeContext(this, field.getAnnotations(), field.getType(), parameterizedType);
        Object object = valueProvider.getValue(context);

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

        Expression expression = new Expression(o, FieldUtils.getGetter(field), new Object[0]);
        expression.execute();
        return expression.getValue() != null;
    }

    private boolean hasGetter(Object o, Field field) {
        return Arrays.stream(o.getClass().getMethods())
                .anyMatch(method -> method.getName().equals(FieldUtils.getGetter(field)));
    }

    private boolean hasSetter(Object o, Field field) {
        return Arrays.stream(o.getClass().getMethods())
                .anyMatch(method -> method.getName().equals(FieldUtils.getSetter(field)));
    }

    public DataFakerTypeFactory getFactory() {
        return factory;
    }


    public DataFakerValueProvider getValueProvider() {
        return valueProvider;
    }

    public Faker getFaker() {
        return faker;
    }
}
