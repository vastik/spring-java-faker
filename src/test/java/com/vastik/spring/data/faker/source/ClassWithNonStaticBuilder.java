package com.vastik.spring.data.faker.source;

import lombok.Data;

@Data
public class ClassWithNonStaticBuilder {
    private Integer id;

    public ClassWithNonStaticBuilderBuilder builder() {
        return new ClassWithNonStaticBuilderBuilder();
    }

    public class ClassWithNonStaticBuilderBuilder {
        public ClassWithNonStaticBuilder build() {
            return new ClassWithNonStaticBuilder();
        }
    }
}
