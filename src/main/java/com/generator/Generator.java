package com.generator;

import com.generator.internal.StringGen;
import com.generator.internal.TestBeforeMethodGen;
import com.generator.internal.TestMethodGen;
import com.settings.SettingState;
import com.settings.SettingsPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.*;
import static java.util.stream.Collectors.toList;

public class Generator {

    public static String packageStatement(String packageName) {
        return "package " + packageName + ";" + System.lineSeparator() + System.lineSeparator();
    }

    public static String importStatement(String importClassStr) {
        return "import " + importClassStr + ";" + System.lineSeparator();
    }

    public static String generateField(String modifier, String className, String fieldName) {
        return "\t" + modifier + " " + className + " " + fieldName + ";" + System.lineSeparator();
    }

    public static String generateTestForClass(Class<?> clazz, int numberOfTests) {

        String srcCode = getTestFileHeader(clazz);

        srcCode += getTestHeader(clazz);

        List<String> methodSourceCode = getTestMethods(clazz, numberOfTests);

        srcCode += String.join(System.lineSeparator(), methodSourceCode);

        srcCode += getTestFooter();

        return srcCode;
    }

    @NotNull
    public static String getTestFooter() {
        return "}" + StringGen.ls;
    }

    @NotNull
    public static List<String> getTestMethods(Class<?> clazz, int numberOfTests) {

        Method[] declaredMethods = Arrays.stream(clazz.getDeclaredMethods())
                .sorted(Comparator.comparing(Method::getName))
                .toArray(Method[]::new);

        return Arrays.stream(declaredMethods)
                .filter(method -> !Modifier.isPrivate(method.getModifiers()))
                .map(TestMethodGen::new)
                .map(TestMethodGen::gen)
                .collect(toList());
    }

    @NotNull
    public static String getTestHeader(Class<?> clazz) {
        String newCode = "public class " + clazz.getSimpleName() + "Test {" + StringGen.ls;
        newCode += "public " + clazz.getSimpleName() + "Test(){}" + StringGen.ls;
        newCode += generateField("public", clazz.getSimpleName(), clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1)) + StringGen.ls;
        TestBeforeMethodGen beforeGen = new TestBeforeMethodGen(clazz, clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1));
        newCode += beforeGen.gen();
        newCode += System.lineSeparator();
        return newCode;
    }

    @NotNull
    public static String getTestFileHeader(Class<?> clazz) {
        String canonicalName = clazz.getCanonicalName();
        String packageName = canonicalName.substring(0, canonicalName.lastIndexOf("."));
        String srcCode = packageStatement(packageName);
        srcCode += importStatement("org.junit.Test");
        srcCode += importStatement("org.junit.Before");
        srcCode += System.lineSeparator();
        return srcCode;
    }

    public static void writeTestsToFiles(List<String> testStrings, List<File> emptyTestFiles) throws IOException {
        if (emptyTestFiles.size() != testStrings.size()) {
            throw new IllegalArgumentException("test files list " +
                    "size not equal to the size of the list of test strings");
        }

        int averageListSize = (emptyTestFiles.size() + testStrings.size()) / 2;

        for (int i = 0; i < averageListSize; i++) {
            File file = emptyTestFiles.get(i);
            Path testFilePath = file.toPath();

            String test = testStrings.get(i);
            Files.deleteIfExists(testFilePath);
            Files.createDirectories(testFilePath.getParent());
            Files.createFile(testFilePath);
            Files.write(testFilePath, Arrays.asList(test.split("\n")));
        }
    }

    public static void generateTests(String classPath, String testDirectory) throws IOException, ClassNotFoundException {

        List<Class<?>> classes = getClasses(classPath);

        SettingsPlugin settings = new SettingsPlugin();
        SettingState settingParameters = settings.getInstance().getState();

        List<String> testStubs = classes.stream()
                .map(clazz -> generateTestForClass(clazz, Integer.parseInt(requireNonNull(settingParameters).getNumberOfTests())))
                .collect(toList());

        List<File> emptyTestFiles = classes.stream()
                .map(Generator::getTestFileName)
                .map(filename -> testDirectory + File.separator + filename)
                .map(File::new)
                .collect(toList());


        writeTestsToFiles(testStubs, emptyTestFiles);
    }

    @NotNull
    public static String getTestFileName(Class<?> clazz) {
        return clazz.getCanonicalName().replace(".", File.separator) + "Test.java";
    }

    @NotNull
    public static ClassLoader getClassLoader(String classpath) throws MalformedURLException {

        File classPathFolder = new File(classpath);

        URL url = classPathFolder.toURI().toURL();
        URL[] urls = new URL[]{url};

        return new URLClassLoader(urls);
    }

    @NotNull
    public static List<Class<?>> getClasses(String compiledClassesPath) throws IOException, ClassNotFoundException {

        Path compiledClassesFolder = Paths.get(compiledClassesPath);

        List<String> classNames;
        try (Stream<Path> walk = Files.walk(compiledClassesFolder)){
            classNames = walk
                    .filter(file -> file.getFileName().toString().endsWith(".class"))
                    .map(path -> getPackageName(
                            path,
                            compiledClassesFolder,
                            path.getFileName().toString()) + path.getFileName().toString().replace(".class", "")
                    )
                    .collect(Collectors.toList());
        }

        ClassLoader classLoader = getClassLoader(compiledClassesPath);

        List<Class<?>> classes = new ArrayList<>();
        for (String className : classNames) {
            classes.add(classLoader.loadClass(className));
        }

        return classes;
    }

    public static String getPackageName(Path filePath, Path basePath, String className) {
        return filePath.toString()
                .replace(basePath.toString(), "")
                .replace(className, "")
                .replaceFirst("/", "")
                .replaceFirst("\\\\", "")
                .replaceAll("\\\\", ".")
                .replaceAll("/", ".");
    }

    public static String binAbsolutePath;

    public static void setBinPath(String absolutePath) {
        binAbsolutePath = absolutePath;
    }
}