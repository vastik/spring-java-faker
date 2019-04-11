package com.vastik.spring.data.faker;

public interface DataTypeFaker<T> {
    T getValue(DataFakeContext context) throws Exception;
}
