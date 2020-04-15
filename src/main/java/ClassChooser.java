import com.intellij.ide.util.AbstractTreeClassChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class ClassChooser extends AbstractTreeClassChooserDialog {

    public ClassChooser(String title, Project project, Class elementClass) {
        super(title, project, elementClass);
    }

    @Override
    protected PsiNamedElement getSelectedFromTreeUserObject(DefaultMutableTreeNode node) {
        return null;
    }

    @NotNull
    @Override
    protected List getClassesByName(String name, boolean checkBoxState, String pattern, GlobalSearchScope searchScope) {
        return null;
    }

    @Override
    public void select(Object aClass) {

    }
}
