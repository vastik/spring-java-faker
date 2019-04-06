package com.vastik.spring.data.faker.test;

import com.vastik.spring.data.faker.DataFaker;
import com.vastik.spring.data.faker.DataFakerTypeFactory;
import com.vastik.spring.data.faker.source.SimpleClass;
import org.junit.Assert;
import org.junit.Test;

public class DataFakerTest {

    public DataFakerTest() {
    }

    @Test
    public void test() throws Exception {
        DataFaker dataFaker = new DataFaker(new DataFakerTypeFactory());
        SimpleClass fake = dataFaker.fake(SimpleClass.class);
        Assert.assertNotNull(fake.getCount());
        Assert.assertTrue(fake.getCount() < 15);
        Assert.assertNotNull(fake.getName());
    }
}
