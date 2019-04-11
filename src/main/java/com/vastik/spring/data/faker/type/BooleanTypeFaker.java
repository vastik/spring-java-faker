package com.vastik.spring.data.faker.type;

import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;

public class BooleanTypeFaker implements DataTypeFaker<Boolean> {

    @Override
    public Boolean getValue(DataFakeContext dataFakeContext) {
        return dataFakeContext.getFaker().bool().bool();
    }
}
