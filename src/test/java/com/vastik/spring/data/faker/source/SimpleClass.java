package com.vastik.spring.data.faker.source;

import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeObject;
import com.vastik.spring.data.faker.annotation.FakeRandom;
import com.vastik.spring.data.faker.annotation.FakeValue;
import lombok.Data;

@Data
public class SimpleClass {

    @FakeRandom(15)
    private Integer count;

    @FakeFaker("gameOfThrones.dragon")
    private String name;

    @FakeValue({"RED", "BLACK"})
    private Colors colors;

    @FakeObject
    private AnotherClass anotherClass;
}
