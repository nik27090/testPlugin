package com.generator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void generate() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator, 1);

        String expectedTestClass = new String(Files.readAllBytes(Paths.get("./src/test/resources/TestDataGeneratorTest.txt")), StandardCharsets.UTF_8);

        assertThat(testForClass).isEqualTo(expectedTestClass);
    }
}