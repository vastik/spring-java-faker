package com.vastik.spring.data.faker;

import com.vastik.spring.data.faker.type.*;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class DataFakerTypeFactory {

    private BidiMap<Class<?>, Class<? extends DataTypeFaker>> classMap = new DualHashBidiMap<>();

    private BidiMap<Class<? extends DataTypeFaker>, DataTypeFaker> instanceClassMap = new DualHashBidiMap<>();

    public DataFakerTypeFactory() {
        this.setTypeFaker(Boolean.class, BooleanTypeFaker.class);
        this.setTypeFaker(Integer.class, IntegerTypeFaker.class);
        this.setTypeFaker(Date.class, DateTypeFaker.class);
        this.setTypeFaker(LocalDateTime.class, LocalDateTimeTypeFaker.class);
        this.setTypeFaker(Long.class, LongTypeFaker.class);
        this.setTypeFaker(String.class, StringTypeFaker.class);
        this.setTypeFaker(Enum.class, EnumTypeFaker.class);
        this.setTypeFaker(List.class, ListTypeFaker.class);
        this.setTypeFaker(Set.class, SetTypeFaker.class);
    }

    public <T> void setTypeFaker(Class<T> type, Class<? extends DataTypeFaker<T>> fakeClass) {
        classMap.put(type, fakeClass);
    }

    public boolean hasTypeFakerForType(Class type) {
        return classMap.containsKey(type);
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

    private <T> DataTypeFaker<T> tryInstantiateClass(Class<? extends DataTypeFaker<T>> typeFakerClass) {
        try {
            return typeFakerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate DataTypeFaker class", e);
        }
    }
}
