package com.vastik.spring.data.faker.source;

import com.vastik.spring.data.faker.annotation.FakeCollection;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeRandom;
import com.vastik.spring.data.faker.annotation.FakeValue;
import lombok.Data;

import java.util.Set;

@Data
public class SimpleClass {

    @FakeRandom(15)
    private Integer count;

    @FakeFaker("gameOfThrones.dragon")
    private String name;

    @FakeValue({"RED", "BLACK"})
    private Colors colors;

    @FakeRandom(25)
    @FakeCollection(min = 5, max = 15)
    private Set<Integer> integers;
}
