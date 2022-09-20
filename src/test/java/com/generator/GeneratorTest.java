package com.generator;

import com.comparators.FuzzyComparator;
import com.comparators.trigram.TrigramGenerator;
import com.comparators.trigram.TrigramMatcher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.assertj.core.api.Assertions.assertThat;

class GeneratorTest {

    @Test
    void generateOld() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator, 1);

        String expectedTestClass = new String(readAllBytes(get("./src/test/resources/TestDataGeneratorTestOld.txt")), UTF_8);

        FuzzyComparator fuzzyComparator = new FuzzyComparator(new TrigramGenerator(), new TrigramMatcher());
        boolean isEqual = fuzzyComparator.compare(testForClass, expectedTestClass);
        assertThat(isEqual).isTrue();
    }

    @Test
    @Disabled
    void generateNew() throws ClassNotFoundException, IOException {
        Class<?> testDataGenerator = ClassLoader.getSystemClassLoader().loadClass("com.testdata.TestDataGenerator");

        String testForClass = Generator.generateTestForClass(testDataGenerator);

        String expectedTes = new String(readAllBytes(get("./src/test/resources/TestDataGeneratorTestNew.txt")), UTF_8);

        assertThat(testForClass).isEqualTo(expectedTes);
    }
}