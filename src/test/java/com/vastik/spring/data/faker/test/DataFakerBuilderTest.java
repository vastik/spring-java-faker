package com.vastik.spring.data.faker.test;

import com.vastik.spring.data.faker.DataFaker;
import com.vastik.spring.data.faker.DataFakerBuilder;
import com.vastik.spring.data.faker.source.ClassWithNonStaticBuilder;
import com.vastik.spring.data.faker.source.SimpleClass;
import com.vastik.spring.data.faker.source.SimpleClassWithBuilder;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DataFakerBuilderTest {

    private static DataFaker dataFaker = new DataFaker();

    @Test(expected = NoSuchMethodException.class)
    public void test_shouldThrowException_whenClassHasNoBuilder() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        DataFakerBuilder<SimpleClass> builder = new DataFakerBuilder<>(SimpleClass.class, dataFaker);
    }

    @Test(expected = IllegalStateException.class)
    public void test_shouldThrowException_whenClassHasNonStaticBuilder() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        DataFakerBuilder<ClassWithNonStaticBuilder> builder = new DataFakerBuilder<>(ClassWithNonStaticBuilder.class, dataFaker);
    }

    @Test(expected = IllegalStateException.class)
    public void test_shouldThrowException_whenClassBuilderHasWrongReturnType() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        DataFakerBuilder<ClassWithNonStaticBuilder> builder = new DataFakerBuilder<>(ClassWithNonStaticBuilder.class, dataFaker);
    }

    @Test
    @SneakyThrows
    public void test_shouldReturnInstance_whenBuilderAvailable() {
        DataFakerBuilder<SimpleClassWithBuilder> builder = new DataFakerBuilder<>(SimpleClassWithBuilder.class, dataFaker);

        SimpleClassWithBuilder instance = builder.build();

        assertNotNull(instance.getAnotherClass());
        assertNotNull(instance.getName());
        assertNotNull(instance.getColors());
        assertThat(instance.getCount(), is(both(greaterThanOrEqualTo(0)).and(lessThan(15))));
        assertThat(instance.getIntegers().size(), is(both(greaterThanOrEqualTo(5)).and(lessThan(15))));
        assertThat(instance.getIntegers(), everyItem(is(both(greaterThanOrEqualTo(0)).and(lessThan(25)))));
    }

}
