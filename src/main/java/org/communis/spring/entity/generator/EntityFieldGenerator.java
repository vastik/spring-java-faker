package org.communis.spring.entity.generator;

import com.github.javafaker.Faker;
import com.github.vastik.spring.extensions.exception.InvalidDataException;
import org.apache.commons.lang3.StringUtils;
import org.communis.spring.entity.generator.annotation.*;
import org.communis.spring.entity.generator.annotation.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class EntityFieldGenerator {

    private Faker faker = new Faker(new Locale("ru"));

    @Autowired
    private EntityStorage entityStorage;

    @Autowired
    private EntityGenerator entityGenerator;

    public void setValue(Object o, Field field) throws Exception {
        if (field.isAnnotationPresent(Transient.class) ||
                field.isAnnotationPresent(Id.class) ||
                field.isAnnotationPresent(FakeIgnore.class))
            return;

        Class<?> fieldType = field.getType();

        if (fieldType.equals(String.class))
            setStringValue(o, field);

        if (fieldType.equals(Long.class))
            setLongValue(o, field);

        if (fieldType.equals(LocalDateTime.class))
            setDateValue(o, field, annotation);

        if (fieldType.equals(Boolean.class))
            field.set(o, faker.bool().bool());


        if (fakes.ignore())
            return;


        if (fakes.value().length > 0 && !fakes.value()[0].isEmpty())
            setValue(o, field, fakes.value()[0]);



        if (fakes.generate())
            setGeneratedEntity(o, field);
    }

    private static <T extends Annotation> Optional<T> getAnnotation(Field field, Class<T> cl) {
        if (field.isAnnotationPresent(cl))
            return Optional.of(field.getAnnotation(cl));

        return Optional.empty();
    }

    class WrappedValue<T> {
        T value;

        void set(T value) {
            this.value = value;
        }

        boolean unset() {
            return value == null;
        }
    }

    private void setStringValue(Object o, Field field) throws Exception {
        final WrappedValue<String> value = new WrappedValue<>();

        getAnnotation(field, FakeFaker.class).ifPresent(v -> value.set(getFakerValue(v.value(), String.class)));

        getAnnotation(field, FakeLetterify.class).ifPresent(v -> value.set(faker.letterify(v.value())));

        getAnnotation(field, FakeBothify.class).ifPresent(v -> value.set(faker.bothify(v.value())));

        getAnnotation(field, FakeNumberify.class).ifPresent(v -> value.set(faker.numerify(v.value())));

        if (value.unset())
            value.set(faker.bothify("?#?#?#?#"));

        field.set(o, value);
    }

    private void setDateValue(Object o, Field field) {
        final WrappedValue<Date> value = new WrappedValue<>();

        getAnnotation(field, FakeBetween.class).ifPresent(v -> {
            Date past = faker.date().past(v.past().value(), v.past().unit());
            Date future = faker.date().future(v.future().value(), v.future().unit());
            value.set(faker.date().between(past, future));
        });

        getAnnotation(field, FakePast.class).ifPresent(v -> value.set(faker.date().past(v.value(),v.unit())));

        getAnnotation(field, FakePast.class).ifPresent(v -> value.set(faker.date().past(v.value(),v.unit())));
    }

    @SuppressWarnings("unchecked")
    private <T> T getFakerValue(String value, Class<T> cl) {
        String[] methods = StringUtils.split(value, '.');
        Object result = faker;
        Class<?> currentClass = faker.getClass();
        for (String method : methods) {
            try {
                result = currentClass.getMethod(method).invoke(result);
                currentClass = result.getClass();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to call faker class", ex);
            }
        }

        if (!currentClass.isAssignableFrom(cl))
            throw new InvalidDataException("Faker value mismatch, required: %s, got %s", cl, currentClass);

        return (T)result;
    }

    public void setFutureValue(Object o, Field field) throws Exception {

        applyDate(o, field, future);
    }

    public void setPastValue(Object o, Field field) throws Exception {
        Date past = faker.date().past(120, TimeUnit.DAYS);
        applyDate(o, field, past);
    }

    public void setBetweenValue(Object o, Field field) throws Exception {
        Date future = faker.date().future(120, TimeUnit.DAYS);
        Date past = faker.date().past(120, TimeUnit.DAYS);
        Date between = faker.date().between(past, future);
        applyDate(o, field, between);
    }

    public void setNowValue(Object o, Field field) throws Exception {
        field.set(o, LocalDateTime.now());
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object o, Field field, String value) throws Exception {
        Enum[] constants = ((Class<? extends Enum>) field.getType()).getEnumConstants();
        Optional<Enum> result = Arrays.stream(constants).filter(c -> c.name().equals(value)).findAny();

        if (!result.isPresent())
            throw new RuntimeException("Unknown enum value " + value + "; available: " + Arrays.deepToString(constants));

        field.set(o, result.get());
    }

    private LocalDateTime convert(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private void setGeneratedEntity(Object o, Field field) throws Exception {
        if (!entityStorage.isEntity(field.getType()))
            return;

        Object o1 = entityGenerator.generate(field.getType().getSimpleName());
        field.set(o, o1);
    }
}
