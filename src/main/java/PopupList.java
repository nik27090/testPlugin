import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PopupList extends BaseListPopupStep<String> {

    public PopupList(String title, ArrayList<String> fruits){
        super(title,fruits);
    }

    @Nullable
    @Override
    public PopupStep onChosen(String selectedValue, boolean finalChoice) {
        System.out.println("Выбрали: "+ selectedValue);
        return PopupStep.FINAL_CHOICE;
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return true;
    }
}
