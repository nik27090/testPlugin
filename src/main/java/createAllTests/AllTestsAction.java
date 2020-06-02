package createAllTests;

import Settings.SettingState;
import Settings.SettingsPlugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import generator.Generator;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class AllTestsAction extends AnAction {

    public static Project project = null;

    @Override
    @SneakyThrows
    public void actionPerformed(AnActionEvent generateAllTestsAction) {

        project = generateAllTestsAction.getProject();
        @SystemIndependent String projectBasePath = requireNonNull(project).getBasePath();

        SettingsPlugin settings = new SettingsPlugin();
        SettingState settingParameters = settings.getInstance().getState();
        @SystemIndependent String localClassFolderPath = requireNonNull(settingParameters).getInputPath();

        String absolutePathToClasses = projectBasePath + localClassFolderPath;

        List<Class> classes = getClasses(absolutePathToClasses);

        String outputPath = settingParameters.getOutputPath();
        Generator generator = new Generator(project, outputPath);
        generator.run(classes,Integer.parseInt(settingParameters.getNumberOfTests()));

        Messages.showMessageDialog(generateAllTestsAction.getProject(), "Test creation completed", "Creator Tests",
                Messages.getInformationIcon());
    }

    @NotNull
    private ClassLoader getClassLoader(String classpath) throws MalformedURLException {

        File classPathFolder = new File(classpath);

        URL url = classPathFolder.toURI().toURL();
        URL[] urls = new URL[]{url};

        return new URLClassLoader(urls);
    }

    @NotNull
    private List<Class> getClasses(String compiledClassesPath) throws IOException, ClassNotFoundException {

        Path compiledClassesFolder = Paths.get(compiledClassesPath);

        List<String> classNames = Files.walk(compiledClassesFolder)
                .filter(file -> file.getFileName().toString().endsWith(".class"))
                .map(Path::toFile)
                .map(file -> getPackageName(
                        file.getPath(),
                        compiledClassesPath,
                        file.getName()) + file.getName().replace(".class", "")
                )
                .collect(Collectors.toList());

        ClassLoader classLoader = getClassLoader(compiledClassesPath);

        List<Class> classes = new ArrayList<>();
        for (String className : classNames) {
            classes.add(classLoader.loadClass(className));
        }

        return classes;
    }

    private String getPackageName(String filePath, String basePath, String className) {
        String packageName = filePath
                .replace(basePath, "")
                .replace(className, "")
                .replaceFirst("\\/", "")
                .replaceFirst("\\\\", "")
                .replaceAll("\\\\", ".")
                .replaceAll("\\/", ".");
        return packageName;
    }
}
