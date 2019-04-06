package com.vastik.spring.data.faker.source;

import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeRandom;
import lombok.Data;

@Data
public class SimpleClass {

    @FakeRandom(15)
    private Integer count;

    @FakeFaker("chuckNorris.fact")
    private String name;
}
