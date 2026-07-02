package gdnwxf;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.util.Objects;

public final class CopyPathSettingsConfigurable implements Configurable {
    private JPanel panel;
    private JComboBox<CopyPathSettingsState.WindowsCopyPathStyle> windowsCopyPathStyleComboBox;
    private JTextField filePrefixTextField;
    private JTextField fileSuffixTextField;
    private JTextField pathPrefixTextField;
    private JSpinner scopeSelectedBeforeLineCountSpinner;
    private JSpinner scopeSelectedAfterLineCountSpinner;

    @Override
    public @Nls String getDisplayName() {
        return "Copy Extra";
    }

    @Override
    public @Nullable JComponent createComponent() {
        panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        int row = 0;

        GridBagConstraints pathFormatLabelConstraints = new GridBagConstraints();
        pathFormatLabelConstraints.gridx = 0;
        pathFormatLabelConstraints.gridy = row;
        pathFormatLabelConstraints.anchor = GridBagConstraints.WEST;
        pathFormatLabelConstraints.insets = new Insets(0, 0, 6, 8);
        formPanel.add(new JLabel(SystemInfo.isWindows ? "Windows copy path format:" : "Copy path format:"), pathFormatLabelConstraints);

        GridBagConstraints comboBoxConstraints = new GridBagConstraints();
        comboBoxConstraints.gridx = 1;
        comboBoxConstraints.gridy = row++;
        comboBoxConstraints.weightx = 1.0;
        comboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        comboBoxConstraints.insets = new Insets(0, 0, 6, 0);
        if (SystemInfo.isWindows) {
            windowsCopyPathStyleComboBox = new JComboBox<>(CopyPathSettingsState.WindowsCopyPathStyle.values());
            formPanel.add(windowsCopyPathStyleComboBox, comboBoxConstraints);
        } else {
            formPanel.add(new JLabel("Default"), comboBoxConstraints);
            GridBagConstraints unixNoteConstraints = new GridBagConstraints();
            unixNoteConstraints.gridx = 0;
            unixNoteConstraints.gridy = row++;
            unixNoteConstraints.gridwidth = 2;
            unixNoteConstraints.anchor = GridBagConstraints.WEST;
            unixNoteConstraints.insets = new Insets(0, 0, 6, 0);
            formPanel.add(new JLabel("Linux, macOS, and Unix use the default IDE path format."), unixNoteConstraints);
        }

        pathPrefixTextField = new JTextField(24);
        addRow(formPanel, row++, "Path prefix:", pathPrefixTextField);

        filePrefixTextField = new JTextField(12);
        fileSuffixTextField = new JTextField(12);
        addTwoFieldRow(formPanel, row++, "File prefix:", filePrefixTextField, "File suffix:", fileSuffixTextField);

        scopeSelectedBeforeLineCountSpinner = createLineCountSpinner();
        scopeSelectedAfterLineCountSpinner = createLineCountSpinner();
        addTwoFieldRow(
                formPanel,
                row++,
                "Scope selected before lines:",
                scopeSelectedBeforeLineCountSpinner,
                "After lines:",
                scopeSelectedAfterLineCountSpinner
        );

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

    private static void addTwoFieldRow(JPanel formPanel, int row, String firstLabel, JComponent firstComponent, String secondLabel, JComponent secondComponent) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 6, 8);
        formPanel.add(new JLabel(firstLabel), labelConstraints);

        JPanel fieldPanel = new JPanel(new GridBagLayout());
        GridBagConstraints firstFieldConstraints = new GridBagConstraints();
        firstFieldConstraints.gridx = 0;
        firstFieldConstraints.gridy = 0;
        firstFieldConstraints.weightx = 1.0;
        firstFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        firstFieldConstraints.insets = new Insets(0, 0, 0, 8);
        fieldPanel.add(firstComponent, firstFieldConstraints);

        GridBagConstraints secondLabelConstraints = new GridBagConstraints();
        secondLabelConstraints.gridx = 1;
        secondLabelConstraints.gridy = 0;
        secondLabelConstraints.anchor = GridBagConstraints.WEST;
        secondLabelConstraints.insets = new Insets(0, 0, 0, 8);
        fieldPanel.add(new JLabel(secondLabel), secondLabelConstraints);

        GridBagConstraints secondFieldConstraints = new GridBagConstraints();
        secondFieldConstraints.gridx = 2;
        secondFieldConstraints.gridy = 0;
        secondFieldConstraints.weightx = 1.0;
        secondFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldPanel.add(secondComponent, secondFieldConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(0, 0, 6, 0);
        formPanel.add(fieldPanel, fieldConstraints);
    }

    private static JSpinner createLineCountSpinner() {
        return new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
    }

    private static int getSpinnerIntValue(JSpinner spinner) {
        try {
            spinner.commitEdit();
        } catch (ParseException ignored) {
            // 保留当前有效值。
        }
        return ((Number) spinner.getValue()).intValue();
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
        if (scopeSelectedBeforeLineCountSpinner != null) {
            modified = modified || getSpinnerIntValue(scopeSelectedBeforeLineCountSpinner) != settings.getScopeSelectedBeforeLineCount();
        }
        if (scopeSelectedAfterLineCountSpinner != null) {
            modified = modified || getSpinnerIntValue(scopeSelectedAfterLineCountSpinner) != settings.getScopeSelectedAfterLineCount();
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
        if (scopeSelectedBeforeLineCountSpinner != null) {
            settings.setScopeSelectedBeforeLineCount(getSpinnerIntValue(scopeSelectedBeforeLineCountSpinner));
        }
        if (scopeSelectedAfterLineCountSpinner != null) {
            settings.setScopeSelectedAfterLineCount(getSpinnerIntValue(scopeSelectedAfterLineCountSpinner));
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
        if (scopeSelectedBeforeLineCountSpinner != null) {
            scopeSelectedBeforeLineCountSpinner.setValue(settings.getScopeSelectedBeforeLineCount());
        }
        if (scopeSelectedAfterLineCountSpinner != null) {
            scopeSelectedAfterLineCountSpinner.setValue(settings.getScopeSelectedAfterLineCount());
        }
    }

    @Override
    public void disposeUIResources() {
        panel = null;
        windowsCopyPathStyleComboBox = null;
        filePrefixTextField = null;
        fileSuffixTextField = null;
        pathPrefixTextField = null;
        scopeSelectedBeforeLineCountSpinner = null;
        scopeSelectedAfterLineCountSpinner = null;
    }
}
