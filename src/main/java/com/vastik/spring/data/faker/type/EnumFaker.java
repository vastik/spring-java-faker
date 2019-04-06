package com.vastik.spring.data.faker.type;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.annotation.FakeValue;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnumFaker extends DataTypeFaker<Enum> {

    public EnumFaker(Faker faker) {
        super(faker);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enum getValue(Field field) {
        return getValue((Class<? extends Enum>) field.getType(), field.getAnnotations());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enum getValue(Method method) {
        return getValue((Class<? extends Enum>) method.getParameters()[0].getType(), method.getAnnotations());
    }

    private Enum getValue(Class<? extends Enum> type, Annotation[] annotations) {
        final WrappedValue<Enum> value = new WrappedValue<>();
        List<? extends Enum> enums = Arrays.asList(type.getEnumConstants());

        AnnotationUtils.getAnnotation(annotations, FakeValue.class)
                .ifPresent(v -> {
                    if (v.value().length == 0)
                        return;

                    if (v.value().length == 1 && v.value()[0].isEmpty())
                        return;

                    List<String> values = Arrays.asList(v.value());
                    String s = values.get(new Random().nextInt(values.size()));

                    Enum enumValue = enums.stream()
                            .filter(e -> e.name().equals(s))
                            .findAny()
                            .orElseThrow(() -> throwMissingEnumException(s, enums));

                    value.set(enumValue);
                });

        if (value.unset())
            value.set(enums.get(new Random().nextInt(enums.size())));

        return value.get();
    }

    private IllegalArgumentException throwMissingEnumException(String target, List<? extends Enum> list) {
        String message = String.format("Enum value %s not found in %s", target, list.toString());
        throw new IllegalArgumentException(message);
    }
}
