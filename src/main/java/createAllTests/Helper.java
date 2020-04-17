package createAllTests;

import Settings.SettingState;
import Settings.SettingsPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;

import java.util.Collection;
import java.util.Objects;

public class Helper {
    public static Collection<VirtualFile> getAllProjectClasses(Project project) {
        //Settings
        PersistentStateComponent<SettingState> state = new SettingsPlugin().getInstance();
        String inputPath = Objects.requireNonNull(state.getState()).inputPath;
        String outputPath = state.getState().outputPath;
        System.out.println("Input Path: " + inputPath + ", Output Path: " + outputPath);

        //search path
        VirtualFile targetPath = LocalFileSystem.getInstance().
                findFileByPath(project.getBasePath() + "/" + inputPath);

        System.out.println("AAAA2 " + targetPath.toString());
        if (targetPath == null) {
            Messages.showMessageDialog(project, "Input path does not exist", "Path Error",
                    Messages.getErrorIcon());
        }
        System.out.println(Objects.requireNonNull(targetPath).getPath());

        GlobalSearchScope globalSearchScope = new GlobalSearchScopesCore.DirectoryScope
                (project, Objects.requireNonNull(targetPath), true);
        System.out.println("AAAAA3 " + globalSearchScope.toString());

        //result
        Collection<VirtualFile> classes = FilenameIndex.getAllFilesByExt(project, "class", globalSearchScope);
        System.out.println("AAAAA4 " + classes.size());

        for (VirtualFile vf : classes) {
            System.out.println("AAAAAA");
            System.out.println(vf.getName());
            System.out.println(vf.getPath());
            System.out.println("AAAAAA1111");
        }

        return classes;
    }
}
