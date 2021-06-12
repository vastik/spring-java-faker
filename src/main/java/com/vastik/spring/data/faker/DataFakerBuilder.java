package com.vastik.spring.data.faker;

import com.github.javafaker.Faker;

import java.beans.Statement;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Optional;

public class DataFakerBuilder<T> {

    public static final String BUILDER_METHOD = "builder";
    public static final String BUILD_METHOD = "build";

    private final Faker faker;
    private final DataFakerRegistry registry;
    private final DataFaker dataFaker;
    private final Class<? extends T> targetClass;
    private final Class<?> builderClass;
    private final Object builder;
    private final Method buildMethod;

    public DataFakerBuilder(Class<? extends T> targetClass, DataFaker dataFaker) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.targetClass = targetClass;
        this.faker = dataFaker.getFaker();
        this.registry = dataFaker.getRegistry();
        this.dataFaker = dataFaker;

        Method builderMethod = targetClass.getMethod(BUILDER_METHOD);
        if (!Modifier.isStatic(builderMethod.getModifiers())) {
            throw new IllegalStateException("Builder method must be static");
        }

        builderClass = builderMethod.getReturnType();

        buildMethod = builderClass.getMethod(BUILD_METHOD);
        if (!buildMethod.getReturnType().equals(targetClass)) {
            throw new IllegalStateException("Build method returns incorrect type");
        }
        builder = builderMethod.invoke(null);
    }

    @SuppressWarnings("unchecked")
    public T build() throws Exception {
        for (Field field : targetClass.getDeclaredFields())
            setValue(builder, field);
        return (T)buildMethod.invoke(builder);
    }

    private void setValue(Object builder, Field field) throws Exception {
        if (!hasMethod(builderClass, field.getName())) {
            return;
        }

        Optional<Object> fakeValue = getDataForField(field);

        if (fakeValue.isPresent()) {
            assignBuilderValue(builder, field, fakeValue.get());
        }
    }

    private boolean hasMethod(Class<?> targetClass, String fieldName) {
        return Arrays.stream(targetClass.getMethods())
                .anyMatch(m -> m.getName().equals(fieldName));
    }

    private Optional<Object> getDataForField(Field field) throws Exception {
        ParameterizedType parameterizedType = null;
        if (field.getGenericType() instanceof ParameterizedType)
            parameterizedType = (ParameterizedType) field.getGenericType();

        DataFakeContext context = new DataFakeContext(dataFaker, faker, field.getAnnotations(), field.getType(), parameterizedType);
        Object object = registry.handle(context);
        return Optional.ofNullable(object);
    }

    private void assignBuilderValue(Object instance, Field field, Object fakeValue) throws Exception {
        Statement statement = new Statement(instance, field.getName(), new Object[]{fakeValue});
        statement.execute();
    }
}

