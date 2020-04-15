package Settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "Settings",
        storages = {@Storage("settings-value-demo.xml")}
)
public class SettingsPlugin implements PersistentStateComponent<SettingState> {

    private SettingState settingState = new SettingState();

    @Nullable
    @Override
    public SettingState getState() {
        return settingState;
    }

    @Override
    public void loadState(@NotNull SettingState state) {
        settingState = state;
    }

    public PersistentStateComponent<SettingState> getInstance() {
        return ServiceManager.getService(SettingsPlugin.class);
    }

}
