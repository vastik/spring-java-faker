package com.vastik.spring.data.faker;

import com.vastik.spring.data.faker.annotation.FakeCustom;
import com.vastik.spring.data.faker.annotation.FakeObject;
import com.vastik.spring.data.faker.utils.AnnotationUtils;

public class DataFakerValueProvider {

    private final DataFakerTypeFactory dataFakerTypeFactory;

    private final DataFaker dataFaker;

    public DataFakerValueProvider(DataFakerTypeFactory dataFakerTypeFactory, DataFaker dataFaker) {
        this.dataFakerTypeFactory = dataFakerTypeFactory;
        this.dataFaker = dataFaker;
    }

    public Object getValue(DataFakeContext context) throws Exception {
        FakeCustom fakeCustom = AnnotationUtils.getAnnotation(context.getAnnotations(), FakeCustom.class).orElse(null);

        if (fakeCustom != null) {
            Class<? extends DataTypeFaker> typeFakerClass = fakeCustom.value();
            DataTypeFaker<?> dataTypeFaker = typeFakerClass.newInstance();
            return dataTypeFaker.getValue(context);
        }

        if (dataFakerTypeFactory.hasTypeFakerForType(context.getType()))
            return dataFakerTypeFactory.getDataTypeFakerForType(context.getType()).getValue(context);
        else if (context.getType().isEnum())
            return dataFakerTypeFactory.getDataTypeFakerForType(Enum.class).getValue(context);
        else if (AnnotationUtils.hasAnnotation(context.getAnnotations(), FakeObject.class))
            return dataFaker.fake(context.getType());

        return null;
    }
}
