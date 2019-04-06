package com.vastik.spring.data.faker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataFaker {

    @Autowired
    private ObjectValueSetter objectValueSetter;

    public <T> T fake(Class<? extends T> targetClass) {
        return null;
    }

    public <T> T fake(T instance) {
        return instance;
    }
}
