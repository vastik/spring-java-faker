package com.vastik.spring.data.faker.type;

class WrappedValue<T> {
    private T value;

    void set(T value) {
        this.value = value;
    }

    boolean unset() {
        return value == null;
    }

    T get() {
        return value;
    }
}
