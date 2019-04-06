package com.vastik.spring.data.faker;

import com.vastik.spring.data.faker.setters.FieldValueSetter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class DataFaker {
    private final FieldValueSetter fieldValueSetter;

    public DataFaker(DataFakerTypeFactory dataFakerTypeFactory) {
        fieldValueSetter = new FieldValueSetter(dataFakerTypeFactory, this);
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
            fieldValueSetter.setValue(instance, field);
    }
}
