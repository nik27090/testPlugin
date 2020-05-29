package Settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SettingsDataWrapper extends DialogWrapper {

    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtInput = new JTextField();
    private JTextField txtOutput = new JTextField();
    private JTextField txtNumberOfTests = new JTextField();

    protected SettingsDataWrapper(boolean canBeParent) {
        super(canBeParent);
        init();
        setTitle("TestPlugin Settings");

        PersistentStateComponent<SettingState> state = new SettingsPlugin().getInstance();
        if (state != null) {
            txtInput.setText(Objects.requireNonNull(state.getState()).inputPath);
            txtOutput.setText(state.getState().outputPath);
            txtNumberOfTests.setText(state.getState().numberOfTests);
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400, 100));

        panel.add(label("Input repository path:"), gb.nextLine().next().weightx(0.2));
        panel.add(txtInput, gb.next().weightx(0.8));

        panel.add(label("Output repository path:"), gb.nextLine().next().weightx(0.2));
        panel.add(txtOutput, gb.next().weightx(0.8));

        panel.add(label("Number of tests"), gb.nextLine().next().weightx(0.2));
        panel.add(txtNumberOfTests, gb.next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String inputText = txtInput.getText();
        String outputText = txtOutput.getText();
        String numTests = txtNumberOfTests.getText();
        System.out.println(inputText);
        System.out.println(outputText);
        System.out.println(numTests);

        PersistentStateComponent<SettingState> state = new SettingsPlugin().getInstance();
        Objects.requireNonNull(state.getState()).inputPath = inputText;
        state.getState().outputPath = outputText;
        state.getState().numberOfTests = numTests;

        close(OK_EXIT_CODE);
    }


    private JComponent label(String text) {
        JBLabel label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(0, 5, 2, 0));
        return label;
    }
}
