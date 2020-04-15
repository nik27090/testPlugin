package Settings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class SettingAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        if (e.getProject() != null){
            SettingsDataWrapper dataWrapper = new SettingsDataWrapper(true);
            if (dataWrapper.showAndGet()){
                dataWrapper.close(23);
            }
        }
    }
}
