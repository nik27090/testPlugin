package com.generator;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void generate() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator, 1);

        String expectedTestClass = new String(readAllBytes(get("./src/test/resources/TestDataGeneratorTest.txt")), UTF_8);

        assertThat(testForClass).isEqualTo(expectedTestClass);
    }
}