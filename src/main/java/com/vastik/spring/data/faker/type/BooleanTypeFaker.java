package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BooleanTypeFaker extends DataTypeFaker<Boolean> {

    public BooleanTypeFaker(Faker faker) {
        super(faker);
    }

    @Override
    public Boolean getValue(Field field) {
        return faker.bool().bool();
    }

    @Override
    public Boolean getValue(Method method) {
        return faker.bool().bool();
    }
}
