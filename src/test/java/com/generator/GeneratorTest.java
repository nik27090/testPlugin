package com.generator;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void generate() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator, 1);

        String expectedTestClass = IOUtils.toString(requireNonNull(ClassLoader.getSystemClassLoader()
                .getResourceAsStream("TestDataGeneratorTest.txt")), UTF_8);

        assertThat(testForClass).isEqualTo(expectedTestClass);
    }
}