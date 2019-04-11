package com.vastik.spring.data.faker.type;

import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;
import com.vastik.spring.data.faker.annotation.FakeValue;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnumTypeFaker implements DataTypeFaker<Enum> {

    @Override
    @SuppressWarnings("unchecked")
    public Enum getValue(DataFakeContext dataFakeContext) {
        return getValue((Class<? extends Enum>) dataFakeContext.getType(), dataFakeContext.getAnnotations());
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
