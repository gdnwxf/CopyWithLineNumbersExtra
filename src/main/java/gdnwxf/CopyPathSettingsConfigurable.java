package gdnwxf;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;

public final class CopyPathSettingsConfigurable implements Configurable {
    private JPanel panel;
    private JComboBox<CopyPathSettingsState.WindowsCopyPathStyle> windowsCopyPathStyleComboBox;
    private JTextField filePrefixTextField;
    private JTextField fileSuffixTextField;
    private JTextField pathPrefixTextField;

    @Override
    public @Nls String getDisplayName() {
        return "Copy Extra";
    }

    @Override
    public @Nullable JComponent createComponent() {
        panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        int row = 0;

        filePrefixTextField = new JTextField(24);
        addRow(formPanel, row++, "File prefix:", filePrefixTextField);

        fileSuffixTextField = new JTextField(24);
        addRow(formPanel, row++, "File suffix:", fileSuffixTextField);

        pathPrefixTextField = new JTextField(24);
        addRow(formPanel, row++, "Path prefix:", pathPrefixTextField);

        GridBagConstraints noteConstraints = new GridBagConstraints();
        noteConstraints.gridx = 0;
        noteConstraints.gridy = row++;
        noteConstraints.gridwidth = 2;
        noteConstraints.anchor = GridBagConstraints.WEST;
        noteConstraints.insets = new Insets(4, 0, 8, 0);
        formPanel.add(new JLabel("Leave prefix/suffix empty to output no File:, Path:, or line suffix."), noteConstraints);

        GridBagConstraints pathFormatLabelConstraints = new GridBagConstraints();
        pathFormatLabelConstraints.gridx = 0;
        pathFormatLabelConstraints.gridy = row;
        pathFormatLabelConstraints.anchor = GridBagConstraints.WEST;
        pathFormatLabelConstraints.insets = new Insets(0, 0, 0, 8);
        formPanel.add(new JLabel(SystemInfo.isWindows ? "Windows copy path format:" : "Copy path format:"), pathFormatLabelConstraints);

        GridBagConstraints comboBoxConstraints = new GridBagConstraints();
        comboBoxConstraints.gridx = 1;
        comboBoxConstraints.gridy = row++;
        comboBoxConstraints.weightx = 1.0;
        comboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        if (SystemInfo.isWindows) {
            windowsCopyPathStyleComboBox = new JComboBox<>(CopyPathSettingsState.WindowsCopyPathStyle.values());
            formPanel.add(windowsCopyPathStyleComboBox, comboBoxConstraints);
        } else {
            formPanel.add(new JLabel("Default"), comboBoxConstraints);
        }

        if (!SystemInfo.isWindows) {
            GridBagConstraints unixNoteConstraints = new GridBagConstraints();
            unixNoteConstraints.gridx = 0;
            unixNoteConstraints.gridy = row;
            unixNoteConstraints.gridwidth = 2;
            unixNoteConstraints.anchor = GridBagConstraints.WEST;
            unixNoteConstraints.insets = new Insets(8, 0, 0, 0);
            formPanel.add(new JLabel("Linux, macOS, and Unix use the default IDE path format."), unixNoteConstraints);
        }

        panel.add(formPanel, BorderLayout.NORTH);
        reset();
        return panel;
    }

    private static void addRow(JPanel formPanel, int row, String label, JComponent component) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 6, 8);
        formPanel.add(new JLabel(label), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(0, 0, 6, 0);
        formPanel.add(component, fieldConstraints);
    }

    @Override
    public boolean isModified() {
        CopyPathSettingsState settings = CopyPathSettingsState.getInstance();
        boolean modified = false;
        if (windowsCopyPathStyleComboBox != null) {
            modified = windowsCopyPathStyleComboBox.getSelectedItem() != settings.getWindowsCopyPathStyle();
        }
        if (filePrefixTextField != null) {
            modified = modified || !Objects.equals(filePrefixTextField.getText(), settings.getFilePrefix());
        }
        if (fileSuffixTextField != null) {
            modified = modified || !Objects.equals(fileSuffixTextField.getText(), settings.getFileSuffix());
        }
        if (pathPrefixTextField != null) {
            modified = modified || !Objects.equals(pathPrefixTextField.getText(), settings.getPathPrefix());
        }
        return modified;
    }

    @Override
    public void apply() {
        CopyPathSettingsState settings = CopyPathSettingsState.getInstance();
        if (windowsCopyPathStyleComboBox != null) {
            settings.setWindowsCopyPathStyle(
                    (CopyPathSettingsState.WindowsCopyPathStyle) windowsCopyPathStyleComboBox.getSelectedItem()
            );
        }
        if (filePrefixTextField != null) {
            settings.setFilePrefix(filePrefixTextField.getText());
        }
        if (fileSuffixTextField != null) {
            settings.setFileSuffix(fileSuffixTextField.getText());
        }
        if (pathPrefixTextField != null) {
            settings.setPathPrefix(pathPrefixTextField.getText());
        }
    }

    @Override
    public void reset() {
        CopyPathSettingsState settings = CopyPathSettingsState.getInstance();
        if (windowsCopyPathStyleComboBox != null) {
            windowsCopyPathStyleComboBox.setSelectedItem(settings.getWindowsCopyPathStyle());
        }
        if (filePrefixTextField != null) {
            filePrefixTextField.setText(settings.getFilePrefix());
        }
        if (fileSuffixTextField != null) {
            fileSuffixTextField.setText(settings.getFileSuffix());
        }
        if (pathPrefixTextField != null) {
            pathPrefixTextField.setText(settings.getPathPrefix());
        }
    }

    @Override
    public void disposeUIResources() {
        panel = null;
        windowsCopyPathStyleComboBox = null;
        filePrefixTextField = null;
        fileSuffixTextField = null;
        pathPrefixTextField = null;
    }
}
