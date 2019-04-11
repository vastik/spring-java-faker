package com.vastik.spring.data.faker.type;

import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.DataFakerValueProvider;
import com.vastik.spring.data.faker.annotation.FakeCollection;
import com.vastik.spring.data.faker.utils.AnnotationUtils;
import org.springframework.util.Assert;

import java.util.Collection;

public class CollectionTypeFaker {

    protected void fakeCollection(Collection<Object> collection, DataFakeContext context) throws Exception {
        FakeCollection annotation = AnnotationUtils.getAnnotation(context.getAnnotations(), FakeCollection.class).orElse(null);

        int min = annotation != null ? annotation.min() : 2;
        int max = annotation != null ? annotation.max() : 10;

        Assert.isTrue(min <= max, "@FakeCollection min value " + min + "is larger than max value " + max);

        Class typeClass = (Class) context.getParameterizedType().getActualTypeArguments()[0];
        DataFakerValueProvider valueProvider = context.getDataFaker().getValueProvider();
        DataFakeContext fakeContext = new DataFakeContext(context.getDataFaker(), context.getAnnotations(), typeClass, null);

        for (int i = min; i <= max; i++) {
            Object o = valueProvider.getValue(fakeContext);
            collection.add(o);
        }
    }
}
