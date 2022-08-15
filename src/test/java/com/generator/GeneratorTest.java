package com.generator;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void generate() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator, 1);

        String expectedTes = IOUtils.resourceToString("TestDataGeneratorTest.txt",
                StandardCharsets.UTF_8, ClassLoader.getSystemClassLoader());

        assertThat(testForClass).isEqualTo(expectedTes);
    }
}