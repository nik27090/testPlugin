package createAllTests;

import Settings.SettingsPlugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import generator.Generator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AllTestsAction extends AnAction {

    Generator generator = new Generator();
    public static Project project = null;

    @Override
    public void actionPerformed(AnActionEvent e) {
//        Collection<VirtualFile> classes = getAllProjectClasses(Objects.requireNonNull(e.getProject()));
        project = e.getProject();
        //путь, где лежать скомпилированные классы
        String path = e.getProject().getBasePath() + new SettingsPlugin().getInstance().getState().inputPath;

        System.out.println("Path: "+path);
        //подбираем все скомпилированные классы
        ArrayList<File> files = new ArrayList<>();
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .forEach((f) -> {
                        if (f.getFileName().toString().endsWith(".class")) {
                            files.add(f.toFile());
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<Class> classes = new ArrayList<>();

        //url до места, откуда подгружаем классы
        URL url = null;
        try {
            url = new File(path).toURI().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        URL[] urls = new URL[]{url};

        //подгружаем классы по имени и названию пакета
        ClassLoader cl = new URLClassLoader(urls);
        Class cls = null;
        try {
            for (File file : files) {
                cls = cl.loadClass(getPackageName(file.getPath(), new File(path).getPath(), file.getName()) + file.getName().replace(".class", ""));
                classes.add(cls);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        //создаем unit тесты по загруженным классам
        generator.run(classes);

        Messages.showMessageDialog(e.getProject(), "Test creation completed", "Creator Tests",
                Messages.getInformationIcon());
    }

    private String getPackageName(String path, String basePath, String className) {
        String packageName = path
                .replace(basePath, "")
                .replace(className, "")
                .replaceFirst("\\/", "")
                .replaceFirst("\\\\", "")
                .replaceAll("\\\\", ".")
                .replaceAll("\\/", ".");
        return packageName;
    }
}
