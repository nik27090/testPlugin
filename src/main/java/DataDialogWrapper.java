import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
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

public class DataDialogWrapper extends DialogWrapper {

    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtMode = new JTextField();
    private JTextField txtUserName = new JTextField();
    private JTextField txtPassword = new JPasswordField();

    protected DataDialogWrapper(boolean canBeParent) {
        super(canBeParent);
        init();
        setTitle("MyIdeaDemo Data");

        PersistentStateComponent<PluginState> state = new PluginSettings().getInstance();
        if (state != null){
            txtMode.setText(Objects.requireNonNull(state.getState()).mode);
        }
        CredentialAttributes credentialAttributes = new CredentialAttributes("MyIdeaPlugin");
        Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);
        txtPassword.setText(credentials != null ? credentials.getPasswordAsString() : null);
        txtUserName.setText(credentials != null ? credentials.getUserName() : null);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));

        panel.add(label("mode"), gb.nextLine().next().weightx(0.2));
        panel.add(txtMode, gb.next().weightx(0.8));

        panel.add(label("username"), gb.nextLine().next().weightx(0.2));
        panel.add(txtUserName, gb.next().weightx(0.8));

        panel.add(label("password"), gb.nextLine().next().weightx(0.2));
        panel.add(txtPassword, gb.next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String mode = txtMode.getText();
        String username = txtUserName.getText();
        String password = txtPassword.getText() ;
        System.out.println(mode + " " + username + " " + password);

        PersistentStateComponent<PluginState> state = new PluginSettings().getInstance();
        Objects.requireNonNull(state.getState()).mode = mode;

        CredentialAttributes credentialAttributes = new CredentialAttributes("MyIdeaPlugin", "myKey");
        Credentials credentials = new Credentials(username, password);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    private JComponent label(String text){
        JBLabel label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(0, 5, 2, 0));
        return label;
    }
}
