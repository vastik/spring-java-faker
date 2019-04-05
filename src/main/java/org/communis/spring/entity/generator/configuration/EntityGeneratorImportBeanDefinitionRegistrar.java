package org.communis.spring.entity.generator.configuration;

import org.communis.spring.entity.generator.EntityController;
import org.communis.spring.entity.generator.EntityGenerator;
import org.communis.spring.entity.generator.EntityStorage;
import org.communis.spring.entity.generator.EntityFieldGenerator;
import org.communis.spring.entity.generator.annotation.EnableEntityGenerator;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EntityGeneratorImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableEntityGenerator.class.getName(), false);

        Class[] basePackageClasses = (Class[]) annotationAttributes.get("basePackageClasses");
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");

        List<String> packages = new ArrayList<>(Arrays.asList(basePackages));
        Arrays.stream(basePackageClasses).forEach(cl -> packages.add(cl.getPackage().getName()));

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(EntityStorage.class);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, packages);
        registry.registerBeanDefinition("entityStorage", beanDefinition);

        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(EntityController.class);
        registry.registerBeanDefinition("entityController", beanDefinition);

        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(EntityGenerator.class);
        registry.registerBeanDefinition("entityGenerator", beanDefinition);

        beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(EntityFieldGenerator.class);
        registry.registerBeanDefinition("fakesValueApplier", beanDefinition);
    }
}
