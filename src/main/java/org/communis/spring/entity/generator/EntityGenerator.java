package org.communis.spring.entity.generator;

import com.github.vastik.spring.extensions.exception.ServiceException;
import org.communis.spring.entity.generator.annotation.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.*;

public class EntityGenerator {

    @Autowired
    private EntityStorage entityStorage;

    @Autowired
    private EntityFieldGenerator entityFieldGenerator;

    public Object generate(String className) {
        Class<?> classByName = null;

        classByName = entityStorage.getClassByName(className);

        Object o;

        try {
            o = generateClass(classByName);
        } catch (Exception ex) {
            throw new ServiceException(ex, "Failed to generate entity class");
        }

        return entityStorage.getRepositoryForClass(classByName).save(o);
    }

    private Object generateClass(Class<?> cl) throws Exception {
        Object o = cl.newInstance();
        List<Field> fields = Arrays.asList(cl.getDeclaredFields());

        fields.forEach(field -> {
            if (field.isAnnotationPresent(Transient.class))
                return;

            if (field.isAnnotationPresent(Generate.class))
                applyFakesValue(o, field);
            else
                applyValue(o, field);
        });

        return o;
    }

    private void applyFakesValue(Object o, Field field) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            Generate annotation = field.getAnnotation(Generate.class);
            entityFieldGenerator.setValue(o, field, annotation);
        } catch (Exception ex) {
            field.setAccessible(accessible);
            throw (RuntimeException) ex;
        }
    }

    private void applyValue(Object o, Field field) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            Class<?> type = field.getType();
            if (type.equals(LocalDateTime.class) || type.equals(Date.class))
                entityFieldGenerator.setBetweenValue(o, field);

            if (type.equals(String.class))
                entityFieldGenerator.setBothifyValue(o, field, "#?#?#?#?#?#?");

            if (type.equals(Boolean.class))
                field.set(o, new Random().nextBoolean());

            if (type.equals(Long.class))
                field.set(o, new Random().nextLong());

            if (type.equals(Integer.class))
                field.set(o, new Random().nextInt());

            if (entityStorage.isEntity(type))
                applyEntityValue(o, field, type);

            if (type.isEnum())
                applyEnumValue(o, field);

            if (type.isAssignableFrom(Set.class)) {
                Class<?> argument =  (Class<?>)((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                if (argument.isEnum())
                    applySetEnumValue(o, field);
                else if ()
            }

        } catch (Exception ex) {
            field.setAccessible(accessible);
            throw (RuntimeException) ex;
        }
    }

    private void applyEnumValue(Object o, Field field) throws Exception {
        Enum[] constants = ((Class<? extends Enum>) field.getType()).getEnumConstants();
        field.set(o, getRandomElement(Arrays.asList(constants)));
    }

    private void applyEntityValue(Object o, Field field, Class cl) throws Exception {
        JpaRepository jpaRepository = entityStorage.getRepositoryForClass(cl);
        List all = jpaRepository.findAll();
        field.set(o, getRandomElement(all));
    }

    private void applySetEnumValue(Object o, Field field) throws Exception {

    }

    private Object getRandomElement(List<?> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
