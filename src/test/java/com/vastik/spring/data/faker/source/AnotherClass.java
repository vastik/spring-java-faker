package com.vastik.spring.data.faker.source;

import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeNumberify;
import com.vastik.spring.data.faker.annotation.FakePast;
import com.vastik.spring.data.faker.annotation.FakeRandomNumber;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Data
public class AnotherClass {
    private String name;

    @FakePast(value = 15, unit = TimeUnit.DAYS)
    LocalDateTime time ;

    @FakeRandomNumber(digits = 7)
    Integer number;

    @FakeNumberify("+38071#######")
    String phoneNumber;

    @FakeFaker("crypto.sha256")
    String hash;
}
