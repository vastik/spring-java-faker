package com.vastik.spring.data.faker;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DataFakerAutoConfiguration.class)
public class DataFakerAutoConfiguration {
}
