import com.intellij.ide.util.AbstractTreeClassChooserDialog;
import com.intellij.ide.util.TreeFileChooserFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopesCore;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.Consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Action extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, true, false, true, true);
        showFileDialog(e);
    }

    private void ListOf(AnActionEvent e) {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("orange");
        arr.add("kekw");
        arr.add("cheb");
        PopupList popupList = new PopupList("my-idea-list", arr);
        if (e.getProject() != null) {
            JBPopupFactory.getInstance().createListPopup(popupList, 5)
                    .showCenteredInCurrentWindow(e.getProject());
        }
    }

    private void showCustomDialog() {
        DataDialogWrapper dataDialogWrapper = new DataDialogWrapper(true);
        if (dataDialogWrapper.showAndGet()){
            dataDialogWrapper.close(23);
        }
    }

    private void getUserName(AnActionEvent e) {
        String name = Messages.showInputDialog(e.getProject(), "Enter your name", "MyIdeaDemo Data", Messages.getQuestionIcon());
        Messages.showMessageDialog(e.getProject(), "Your name is" + name, "MyIdeaDemo", Messages.getInformationIcon());
    }

    private void showFileDialog(AnActionEvent e) {
        // Messages.showMessageDialog(e.getProject(), "Hello World", "MyIdeaDemo", Messages.getInformationIcon());
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(
                true, true, false, false, false, true
        );
        fileChooserDescriptor.setShowFileSystemRoots(true);
        fileChooserDescriptor.setTitle("MyIdeaDemo Pick Directory");
        fileChooserDescriptor.setDescription("My file chooser demo");

        System.out.println("BasePath: " + Objects.requireNonNull(e.getProject()).getBasePath());

        VirtualFile targetPath = LocalFileSystem.getInstance().
                findFileByPath(Objects.requireNonNull(e.getProject()).getBasePath() + "/target/classes");
        System.out.println(Objects.requireNonNull(targetPath).getName());

        GlobalSearchScope globalSearchScope = new GlobalSearchScopesCore.DirectoryScope
                (Objects.requireNonNull(e.getProject()), Objects.requireNonNull(targetPath), true);
        System.out.println(globalSearchScope.toString());

        Collection<VirtualFile> classes = FilenameIndex.getAllFilesByExt(e.getProject(), "class", globalSearchScope);
        System.out.println(classes.toString());
        for (VirtualFile vf: classes) {
            System.out.println(vf.getName());
        }

        FileChooser.chooseFiles(fileChooserDescriptor, e.getProject(), null, new Consumer<List<VirtualFile>>() {
            @Override
            public void consume(List<VirtualFile> files) {
                for (VirtualFile vf: files) {
                    Messages.showMessageDialog(e.getProject(), vf.getCanonicalPath(), "Path", Messages.getInformationIcon());
                }
            }
        });
    }
}
