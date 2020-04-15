package Settings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class SettingAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (e.getProject() != null) {
            SettingsDataWrapper dataWrapper = new SettingsDataWrapper(true);
            if (dataWrapper.showAndGet()) {
                Messages.showMessageDialog(e.getProject(), "Settings have been saved", "Settings",
                        Messages.getInformationIcon());
            }
        }
    }
}
