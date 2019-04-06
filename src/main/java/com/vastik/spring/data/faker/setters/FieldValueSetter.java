package com.vastik.spring.data.faker.setters;

import com.vastik.spring.data.faker.DataFaker;
import com.vastik.spring.data.faker.DataFakerTypeFactory;
import com.vastik.spring.data.faker.annotation.FakeCustom;
import com.vastik.spring.data.faker.annotation.FakeIgnore;
import com.vastik.spring.data.faker.annotation.FakeObject;
import com.vastik.spring.data.faker.annotation.FakeOverride;
import com.vastik.spring.data.faker.type.DataTypeFaker;
import com.vastik.spring.data.faker.utils.FieldUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.beans.Expression;
import java.beans.Statement;
import java.lang.reflect.Field;
import java.util.Arrays;

@Log4j2
public class FieldValueSetter {

    private final DataFakerTypeFactory dataFakerTypeFactory;

    private final DataFaker dataFaker;

    public FieldValueSetter(DataFakerTypeFactory dataFakerTypeFactory, DataFaker dataFaker) {
        this.dataFakerTypeFactory = dataFakerTypeFactory;
        this.dataFaker = dataFaker;
    }

    public void setValue(Object o, Field field) throws Exception {
        if (!field.isAccessible() && !hasSetter(o, field))
            return;

        if (field.isAnnotationPresent(FakeIgnore.class))
            return;

        if (field.isAnnotationPresent(FakeOverride.class) && !canOverride(o, field))
            return;

        Object object = getValue(field);
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

    @SuppressWarnings("unchecked")
    private Object getValue(Field field) throws Exception {
        if (field.isAnnotationPresent(FakeCustom.class)) {
            Class<? extends DataTypeFaker> typeFakerClass = field.getAnnotation(FakeCustom.class).value();
            DataTypeFaker<?> dataTypeFaker = dataFakerTypeFactory.getDataTypeFaker(typeFakerClass);
            return dataTypeFaker.getValue(field);
        }

        if (dataFakerTypeFactory.hasTypeFakerForType(field.getType()))
            return dataFakerTypeFactory.getDataTypeFakerForType(field.getType()).getValue(field);
        else if (field.isEnumConstant())
            return dataFakerTypeFactory.getDataTypeFakerForType(Enum.class).getValue(field);
        else if (field.isAnnotationPresent(FakeObject.class))
            return dataFaker.fake(field.getType());

        return null;
    }
}
