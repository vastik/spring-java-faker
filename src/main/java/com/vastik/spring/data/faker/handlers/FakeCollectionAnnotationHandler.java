package com.vastik.spring.data.faker.handlers;

import com.vastik.spring.data.faker.AnnotationHandler;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.annotation.FakeCollection;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class FakeCollectionAnnotationHandler implements AnnotationHandler<FakeCollection> {

    @Override
    public Object get(FakeCollection annotation, DataFakeContext context) throws Exception {
        Collection<Object> collection = null;

        if (context.getType().equals(List.class))
            collection = new ArrayList<>();

        if (context.getType().equals(Set.class))
            collection = new HashSet<>();

        if (collection == null)
            throw new IllegalArgumentException("Unsupported collection type " + context.getType());

        if (annotation.min() > annotation.max())
            throw new IllegalArgumentException("Invalid collection size");

        Class typeClass = (Class) context.getParameterizedType().getActualTypeArguments()[0];
        DataFakeContext fakeContext = new DataFakeContext(context.getDataFaker(),
                context.getFaker(),
                excludeSelfAnnotation(context.getAnnotations()),
                typeClass,
                null);

        int max = ThreadLocalRandom.current().nextInt(annotation.min(), annotation.max() + 1);
        for (int i = 0; i < max; i++) {
            Object o = context.getDataFaker().getRegistry().handle(fakeContext);
            collection.add(o);
        }

        return collection;
    }

    private Annotation[] excludeSelfAnnotation(Annotation[] annotations) {
        List<Annotation> list = new ArrayList<>(Arrays.asList(annotations));
        list.removeIf(annotation -> annotation.annotationType().equals(FakeCollection.class));
        return list.toArray(new Annotation[0]);
    }
}
