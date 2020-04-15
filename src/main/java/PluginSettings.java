import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "MyIdeaDemo",
        storages = { @Storage("my-value-demo.xml") }
)
public class PluginSettings implements PersistentStateComponent<PluginState> {

    private PluginState pluginState = new PluginState();

    @Nullable
    @Override
    public PluginState getState() {
        return pluginState;
    }

    @Override
    public void loadState(@NotNull PluginState state) {
        pluginState = state;
    }

    public PersistentStateComponent<PluginState> getInstance(){
        return ServiceManager.getService(PluginSettings.class);
    }

}
