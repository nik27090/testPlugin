package com.generator;

import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    @Disabled
    void generateOld() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator, 1);

        String expectedTestClass = new String(readAllBytes(get("./src/test/resources/TestDataGeneratorTest.txt")), UTF_8);

        assertThat(testForClass).isEqualTo(expectedTestClass);
    }

    @Test
    void generateNew() throws ClassNotFoundException, IOException, NoSuchMethodException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator);

        String expectedTes = new String(readAllBytes(get("./src/test/resources/TestDataGeneratorTest.txt")), UTF_8);

        assertThat(testForClass).isEqualTo(expectedTes);
    }
}