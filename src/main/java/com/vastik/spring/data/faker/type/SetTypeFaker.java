package com.vastik.spring.data.faker.type;

import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;

import java.util.HashSet;
import java.util.Set;

public class SetTypeFaker extends CollectionTypeFaker implements DataTypeFaker<Set> {

    @Override
    public Set<Object> getValue(DataFakeContext dataFakeContext) throws Exception {
        final Set<Object> set = new HashSet<>();
        fakeCollection(set, dataFakeContext);
        return set;
    }
}
