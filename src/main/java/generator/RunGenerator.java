package generator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static generator.Generator.*;
import static java.util.stream.Collectors.toList;

public class RunGenerator implements ITestingTool {
    private ClassLoader classLoader;

    @Override
    public List<File> getExtraClassPath() {
        return null;
    }


    @Override
    public void initialize(File src, File bin, List<File> classPath) throws MalformedURLException {
        this.classLoader = getClassLoader(bin.getPath());
        Generator.setBinPath(bin.getAbsolutePath());
    }

    @Override
    public void run(String cName, long timeBudget) throws ClassNotFoundException, IOException {
        Class<?> aClass = classLoader.loadClass(cName);
        int numberOfTests = 1;
        String test = generateTestForClass(aClass, numberOfTests);
        String testFileName = getTestFileName(aClass);
        List<String> collect = Arrays.stream(test.split("\n")).collect(toList());

        Path path = Paths.get("temp/testcases/" + testFileName);
        Files.deleteIfExists(path);
        Files.createDirectories(path.toAbsolutePath().getParent());
        Files.createFile(path);
        Files.write(path, collect);
    }
}
