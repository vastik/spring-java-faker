package com.vastik.spring.data.faker.type;

import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataTypeFaker;

import java.util.ArrayList;
import java.util.List;

public class ListTypeFaker extends CollectionTypeFaker implements DataTypeFaker<List> {

    @Override
    public List<Object> getValue(DataFakeContext dataFakeContext) throws Exception {
        ArrayList<Object> collection = new ArrayList<>();
        fakeCollection(collection, dataFakeContext);
        return collection;
    }
}
