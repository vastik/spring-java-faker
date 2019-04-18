package com.vastik.spring.data.faker.handlers;

import com.vastik.spring.data.faker.AnnotationHandler;
import com.vastik.spring.data.faker.DataFakeContext;
import com.vastik.spring.data.faker.annotation.FakeFaker;
import org.apache.commons.lang3.StringUtils;

public class FakeFakerAnnotationHandler implements AnnotationHandler<FakeFaker> {

    @Override
    public Object get(FakeFaker annotation, DataFakeContext context) throws Exception {
        String[] methods = StringUtils.split(annotation.value(), '.');
        Object result = context.getFaker();
        Class<?> currentClass = result.getClass();

        for (String method : methods) {
            try {
                result = currentClass.getMethod(method).invoke(result);
                currentClass = result.getClass();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to call faker class", ex);
            }
        }

        return result;
    }
}
