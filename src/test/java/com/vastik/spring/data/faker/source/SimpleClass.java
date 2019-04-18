package com.vastik.spring.data.faker.source;

import com.vastik.spring.data.faker.annotation.*;
import lombok.Data;

import java.util.Set;

@Data
public class SimpleClass {

    @FakeNumberRandom(15)
    private int count;

    @FakeFaker("gameOfThrones.dragon")
    private String name;

    @FakeEnum({"RED", "BLACK"})
    private Colors colors;

    @FakeNumberRandom(25)
    @FakeCollection(min = 5, max = 15)
    private Set<Integer> integers;

    @FakeInclude
    private AnotherClass anotherClass;
}
