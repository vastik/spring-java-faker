package com.vastik.spring.data.faker;

import com.vastik.spring.data.faker.annotation.FakeCustom;
import com.vastik.spring.data.faker.annotation.FakeIgnore;
import com.vastik.spring.data.faker.annotation.FakeOverride;
import com.vastik.spring.data.faker.type.DataTypeFaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

@Component
public class ObjectValueSetter {

    @Autowired
    private DataFakerTypeFactory dataFakerTypeFactory;

    @SuppressWarnings("unchecked")
    public void setValue(Object o, Field field) throws Exception {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        if (field.isAnnotationPresent(FakeIgnore.class))
            return;

        if (field.get(o) != null && !field.isAnnotationPresent(FakeOverride.class))
            return;

        try {
            if (field.isAnnotationPresent(FakeCustom.class)) {
                Class<? extends DataTypeFaker> typeFakerClass = field.getAnnotation(FakeCustom.class).value();
                DataTypeFaker<?> dataTypeFaker = dataFakerTypeFactory.getDataTypeFaker(typeFakerClass);
                Object value = dataTypeFaker.getValue(field.getAnnotations());
                field.set(o, value);
            }
            else {
                Class<?> fieldType = field.getType();
                Object value = dataFakerTypeFactory.getDataTypeFakerForType(fieldType).getValue(field.getAnnotations());
                field.set(o, value);
            }
        } finally {
            field.setAccessible(accessible);
        }
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object o, Field field, String value) throws Exception {
        Enum[] constants = ((Class<? extends Enum>) field.getType()).getEnumConstants();
        Optional<Enum> result = Arrays.stream(constants).filter(c -> c.name().equals(value)).findAny();

        if (!result.isPresent())
            throw new RuntimeException("Unknown enum value " + value + "; available: " + Arrays.deepToString(constants));

        field.set(o, result.get());
    }
}
