package com.vastik.spring.data.faker.source;

import com.vastik.spring.data.faker.annotation.FakeDatePast;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import com.vastik.spring.data.faker.annotation.FakeNumberDigits;
import com.vastik.spring.data.faker.annotation.FakeNumberify;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Data
public class AnotherClass {
    private String name;

    @FakeDatePast(value = 15, unit = TimeUnit.DAYS)
    LocalDateTime time ;

    @FakeNumberDigits(7)
    Integer number;

    @FakeNumberify("+38071#######")
    String phoneNumber;

    @FakeFaker("crypto.sha256")
    String hash;
}
