package createAllTests;

import Settings.SettingsPlugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import generator.Generator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static createAllTests.Helper.getAllProjectClasses;

public class AllTestsAction extends AnAction {

    Generator generator = new Generator();

    @Override
    public void actionPerformed(AnActionEvent e) {
//        Collection<VirtualFile> classes = getAllProjectClasses(Objects.requireNonNull(e.getProject()));

        String path  = e.getProject().getBasePath()+new SettingsPlugin().getInstance().getState().inputPath;

        ArrayList<File> files = new ArrayList<>();
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .forEach((f)->{
                        if(f.getFileName().toString().endsWith(".class")){
                            files.add(f.toFile());
                        }
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ArrayList<Class> classes = new ArrayList<>();
        File file = new File(path);

        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        URL[] urls = new URL[]{url};

        ClassLoader cl = new URLClassLoader(urls);
        Class  cls = null;
        try {
            for(File file1 : files) {
                cls = cl.loadClass(getPackageName(file1.getPath(), path, file1.getName())+file1.getName().replace(".class",""));

                classes.add(cls);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        ProtectionDomain pDomain = cls.getProtectionDomain();
        CodeSource cSource = pDomain.getCodeSource();
        URL urlfrom = cSource.getLocation();
        System.out.println(urlfrom.getFile());


        System.out.println("FFFF "+classes.size());

        generator.run(classes);

        Messages.showMessageDialog(e.getProject(), "Test creation completed", "Creator Tests",
                Messages.getInformationIcon());
    }

    private String getPackageName(String path, String basePath, String className){
        String packageName  = path
                .replace(basePath,"")
                .replace(className, "")
                .replaceFirst("\\/","")
                .replaceAll("\\/", ".");
        return packageName;
    }
}
