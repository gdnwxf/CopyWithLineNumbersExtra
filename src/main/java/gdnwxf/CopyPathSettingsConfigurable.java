package gdnwxf;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public final class CopyPathSettingsConfigurable implements Configurable {
    private JPanel panel;
    private JComboBox<CopyPathSettingsState.WindowsCopyPathStyle> windowsCopyPathStyleComboBox;

    @Override
    public @Nls String getDisplayName() {
        return "Copy Extra";
    }

    @Override
    public @Nullable JComponent createComponent() {
        panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 0, 8);
        formPanel.add(new JLabel(SystemInfo.isWindows ? "Windows copy path format:" : "Copy path format:"), labelConstraints);

        GridBagConstraints comboBoxConstraints = new GridBagConstraints();
        comboBoxConstraints.gridx = 1;
        comboBoxConstraints.gridy = 0;
        comboBoxConstraints.weightx = 1.0;
        comboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        if (SystemInfo.isWindows) {
            windowsCopyPathStyleComboBox = new JComboBox<>(CopyPathSettingsState.WindowsCopyPathStyle.values());
            formPanel.add(windowsCopyPathStyleComboBox, comboBoxConstraints);
        } else {
            formPanel.add(new JLabel("Default"), comboBoxConstraints);
        }

        if (!SystemInfo.isWindows) {
            GridBagConstraints noteConstraints = new GridBagConstraints();
            noteConstraints.gridx = 0;
            noteConstraints.gridy = 1;
            noteConstraints.gridwidth = 2;
            noteConstraints.anchor = GridBagConstraints.WEST;
            noteConstraints.insets = new Insets(8, 0, 0, 0);
            formPanel.add(new JLabel("Linux, macOS, and Unix use the default IDE path format."), noteConstraints);
        }

        panel.add(formPanel, BorderLayout.NORTH);
        reset();
        return panel;
    }

    @Override
    public boolean isModified() {
        if (windowsCopyPathStyleComboBox == null) {
            return false;
        }
        return windowsCopyPathStyleComboBox.getSelectedItem()
                != CopyPathSettingsState.getInstance().getWindowsCopyPathStyle();
    }

    @Override
    public void apply() {
        if (windowsCopyPathStyleComboBox == null) {
            return;
        }
        CopyPathSettingsState.getInstance().setWindowsCopyPathStyle(
                (CopyPathSettingsState.WindowsCopyPathStyle) windowsCopyPathStyleComboBox.getSelectedItem()
        );
    }

    @Override
    public void reset() {
        if (windowsCopyPathStyleComboBox == null) {
            return;
        }
        windowsCopyPathStyleComboBox.setSelectedItem(CopyPathSettingsState.getInstance().getWindowsCopyPathStyle());
    }

    @Override
    public void disposeUIResources() {
        panel = null;
        windowsCopyPathStyleComboBox = null;
    }
}
