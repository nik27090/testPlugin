package createCustomTest;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Consumer;

import java.util.List;

public class CustomTestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(
                true, true, false, false, false, true
        );
        fileChooserDescriptor.setShowFileSystemRoots(true);
        fileChooserDescriptor.setTitle("Test Plugin");
        fileChooserDescriptor.setDescription("Select .class files");

        FileChooser.chooseFiles(fileChooserDescriptor, e.getProject(), null, new Consumer<List<VirtualFile>>() {
            @Override
            public void consume(List<VirtualFile> files) {
                //todo
                for (VirtualFile vf : files) {
                    String path = vf.getPath();
                    if (path.endsWith(".class")) {
                        Messages.showMessageDialog(e.getProject(), "Test created for " + path,
                                "Path", Messages.getInformationIcon());
                    }
                }
            }
        });
    }
}
