package createOneTest;

import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ClassPopupList extends BaseListPopupStep<String> {

    public ClassPopupList(String title, ArrayList<String> classes) {
        super(title, classes);
    }

    @Nullable
    @Override
    public PopupStep onChosen(String selectedValue, boolean finalChoice) {
        //todo
        System.out.println("Выбрали: " + selectedValue);
        return PopupStep.FINAL_CHOICE;
    }

    @Override
    public boolean isSpeedSearchEnabled() {
        return true;
    }
}
