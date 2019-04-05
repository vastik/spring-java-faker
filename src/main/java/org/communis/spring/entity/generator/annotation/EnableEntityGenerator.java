package org.communis.spring.entity.generator.annotation;

import org.communis.spring.entity.generator.configuration.EntityGeneratorImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import(EntityGeneratorImportBeanDefinitionRegistrar.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableEntityGenerator {
    String[] basePackages() default {};
    Class<?>[] basePackageClasses() default {};
}
