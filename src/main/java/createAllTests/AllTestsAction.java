package createAllTests;

import Settings.SettingState;
import Settings.SettingsPlugin;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;

import java.util.Collection;
import java.util.Objects;

public class AllTestsAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //Settings
        PersistentStateComponent<SettingState> state = new SettingsPlugin().getInstance();
        String inputPath = Objects.requireNonNull(state.getState()).inputPath;
        String outputPath = state.getState().outputPath;
        System.out.println("Input Path: " + inputPath + ", Output Path: " + outputPath);

        //search path
        VirtualFile targetPath = LocalFileSystem.getInstance().
                findFileByPath(Objects.requireNonNull(e.getProject()).getBasePath() + "/" + inputPath);
        if (targetPath == null) {
            Messages.showMessageDialog(e.getProject(), "Input path does not exist", "Path Error",
                    Messages.getErrorIcon());
        }
        System.out.println(Objects.requireNonNull(targetPath).getPath());

        GlobalSearchScope globalSearchScope = new GlobalSearchScopesCore.DirectoryScope
                (Objects.requireNonNull(e.getProject()), Objects.requireNonNull(targetPath), true);
        System.out.println(globalSearchScope.toString());

        //result
        Collection<VirtualFile> classes = FilenameIndex.getAllFilesByExt(e.getProject(), "class", globalSearchScope);
        for (VirtualFile vf : classes) {
            System.out.println(vf.getName());
        }

        Messages.showMessageDialog(e.getProject(), "Test creation completed", "Creator Tests",
                Messages.getInformationIcon());
    }
}
