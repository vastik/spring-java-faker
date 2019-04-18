package com.vastik.spring.data.faker;

import com.github.javafaker.Faker;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

public class DataFakeContext {
    private final Faker faker;
    private final DataFaker dataFaker;
    private final Annotation[] annotations;
    private final Class<?> type;
    private final ParameterizedType parameterizedType;

    public DataFakeContext(DataFaker dataFaker,
                           Faker faker, Annotation[] annotations,
                           Class<?> type,
                           ParameterizedType parameterizedType) {
        this.dataFaker = dataFaker;
        this.faker = faker;
        this.annotations = annotations;
        this.type = type;
        this.parameterizedType = parameterizedType;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public Class<?> getType() {
        return type;
    }

    public ParameterizedType getParameterizedType() {
        return parameterizedType;
    }

    public Faker getFaker() {
        return faker;
    }

    public DataFaker getDataFaker() {
        return dataFaker;
    }
}
