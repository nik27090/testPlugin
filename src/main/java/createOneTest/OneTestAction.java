package createOneTest;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static createAllTests.Helper.getAllProjectClasses;

public class OneTestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Collection<VirtualFile> classes = getAllProjectClasses(Objects.requireNonNull(e.getProject()));
        ArrayList<String> arr = new ArrayList<>();

        for (VirtualFile vf : classes) {
            arr.add(vf.getName());
        }

        ClassPopupList popupList = new ClassPopupList("Choose a class", arr);
        if (e.getProject() != null) {
            JBPopupFactory.getInstance().createListPopup(popupList, arr.size())
                    .showCenteredInCurrentWindow(e.getProject());
        }
    }
}
