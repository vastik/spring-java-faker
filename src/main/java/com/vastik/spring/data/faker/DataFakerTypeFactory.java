package com.vastik.spring.data.faker;

import com.github.javafaker.Faker;
import com.vastik.spring.data.faker.type.DataTypeFaker;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DataFakerTypeFactory implements EnvironmentAware {

    private Faker faker;

    private BidiMap<Class<?>, Class<? extends DataTypeFaker>> classMap = new DualHashBidiMap<>();

    private BidiMap<Class<? extends DataTypeFaker>, DataTypeFaker> instanceClassMap = new DualHashBidiMap<>();

    public <T> void setTypeFaker(Class<T> type, Class<? extends DataTypeFaker<T>> fakeClass) {
        classMap.put(type, fakeClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends DataTypeFaker<T>> DataTypeFaker<T> getDataTypeFaker(Class<T> type) {
        if (instanceClassMap.get(type) != null)
            return instanceClassMap.get(type);

        if (classMap.getKey(type) == null)
            throw new IllegalArgumentException("DataTypeFaker for type " + type.getName() + " not found");

        DataTypeFaker<T> faker = tryInstantiateClass((Class<? extends DataTypeFaker<T>>) classMap.getKey(type));
        instanceClassMap.put(type, faker);
        return faker;
    }

    @SuppressWarnings("unchecked")
    public <T> DataTypeFaker<T> getDataTypeFakerForType(Class<T> type) {
        if (classMap.get(type) == null)
            throw new IllegalArgumentException("DataTypeFaker for type " + type.getName() + " is not found");

        Class<? extends DataTypeFaker> typeClass = classMap.get(type);

        if (instanceClassMap.get(typeClass) != null)
            return instanceClassMap.get(typeClass);

        DataTypeFaker<T> faker = tryInstantiateClass((Class<? extends DataTypeFaker<T>>) classMap.get(type));
        instanceClassMap.put(typeClass, faker);
        return faker;
    }

    @SuppressWarnings("unchecked")
    private <T> DataTypeFaker<T> tryInstantiateClass(Class<? extends DataTypeFaker<T>> typeFakerClass) {
        try {
            new Faker().gameOfThrones().character();
            return typeFakerClass.getConstructor(Faker.class).newInstance(faker);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate DataTypeFaker class", e);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        String locale = environment.getProperty("spring.faker.locale", "ru");
        this.faker = new Faker(new Locale(locale));
    }
}
