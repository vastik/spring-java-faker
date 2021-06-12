package com.vastik.spring.data.faker.source;

import lombok.Data;

@Data
public class ClassWithMalformedBuilder {
    private Integer id;

    public static ClassWithMalformedBuilder.ClassWithMalformedBuilderBuilder builder() {
        return new ClassWithMalformedBuilder.ClassWithMalformedBuilderBuilder();
    }

    public static class ClassWithMalformedBuilderBuilder {
        public SimpleClass build() {
            return new SimpleClass();
        }
    }
}
