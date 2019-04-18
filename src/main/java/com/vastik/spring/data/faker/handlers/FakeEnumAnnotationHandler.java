package com.vastik.spring.data.faker.handlers;

import com.vastik.spring.data.faker.AnnotationHandler;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.annotation.FakeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FakeEnumAnnotationHandler implements AnnotationHandler<FakeEnum> {

    @Override
    @SuppressWarnings("unchecked")
    public Object get(FakeEnum annotation, DataFakeContext context) throws Exception {
        Class<? extends Enum> type = (Class<? extends Enum>) context.getType();
        List<? extends Enum> values = Arrays.asList(type.getEnumConstants());

        if (annotation.value().length == 0 || annotation.value()[0].isEmpty())
            return values.get(new Random().nextInt(values.size()));

        String s = Arrays.asList(annotation.value()).get(new Random().nextInt(values.size() - 1));
        return values.stream()
                .filter(e -> e.name().equals(s))
                .findAny()
                .orElseThrow(() -> throwMissingEnumException(s, values));
    }

    private IllegalArgumentException throwMissingEnumException(String target, List<? extends Enum> list) {
        String message = String.format("Enum value %s not found in %s", target, list.toString());
        throw new IllegalArgumentException(message);
    }
}
