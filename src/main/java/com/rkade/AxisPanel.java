package com.rkade;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.Serializable;

public class AxisPanel implements Serializable {
    private JLabel minLabel;
    private JTextField minText;
    private JLabel centerLabel;
    private JTextField centerText;
    private JProgressBar progress;
    private JLabel maxLabel;
    private JTextField maxText;
    private JTextField dzText;
    private JLabel valueLabel;
    private JTextField valueText;
    private JLabel rawLabel;
    private JTextField rawText;
    private JLabel deadZoneLabel;
    private JPanel mainPanel;
    private JButton setMinButton;
    private JButton setCenterButton;
    private JButton setMaxButton;
    private JCheckBox autoLimitcheckBox;
    private JCheckBox hasCenterCheckBox;
    private JCheckBox enabledCheckBox;
    private JComboBox trimComboBox;

    public JComponent getRootComponent() {
        return mainPanel;
    }

    public JLabel getMinLabel() {
        return minLabel;
    }

    public JTextField getMinText() {
        return minText;
    }

    public JLabel getCenterLabel() {
        return centerLabel;
    }

    public JTextField getCenterText() {
        return centerText;
    }

    public JProgressBar getProgress() {
        return progress;
    }

    public JLabel getMaxLabel() {
        return maxLabel;
    }

    public JTextField getMaxText() {
        return maxText;
    }

    public JTextField getDzText() {
        return dzText;
    }

    public JLabel getValueLabel() {
        return valueLabel;
    }

    public JTextField getValueText() {
        return valueText;
    }

    public JLabel getRawLabel() {
        return rawLabel;
    }

    public JTextField getRawText() {
        return rawText;
    }

    public JLabel getDeadZoneLabel() {
        return deadZoneLabel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setAlignmentX(1.0f);
        mainPanel.setAlignmentY(1.0f);
        mainPanel.setDoubleBuffered(true);
        mainPanel.setMaximumSize(new Dimension(1020, 110));
        mainPanel.setMinimumSize(new Dimension(1020, 110));
        mainPanel.setPreferredSize(new Dimension(1020, 110));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Axis", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        minLabel = new JLabel();
        minLabel.setHorizontalAlignment(4);
        minLabel.setMinimumSize(new Dimension(26, 17));
        minLabel.setPreferredSize(new Dimension(26, 17));
        minLabel.setText("Min");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(minLabel, gbc);
        centerLabel = new JLabel();
        centerLabel.setHorizontalAlignment(4);
        centerLabel.setHorizontalTextPosition(2);
        centerLabel.setMinimumSize(new Dimension(63, 17));
        centerLabel.setPreferredSize(new Dimension(63, 17));
        centerLabel.setText("Center");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(centerLabel, gbc);
        centerText = new JTextField();
        centerText.setHorizontalAlignment(2);
        centerText.setPreferredSize(new Dimension(65, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(centerText, gbc);
        maxLabel = new JLabel();
        maxLabel.setHorizontalAlignment(4);
        maxLabel.setText("Max");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(maxLabel, gbc);
        minText = new JTextField();
        minText.setHorizontalAlignment(2);
        minText.setPreferredSize(new Dimension(65, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(minText, gbc);
        maxText = new JTextField();
        maxText.setHorizontalAlignment(2);
        maxText.setPreferredSize(new Dimension(65, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(maxText, gbc);
        deadZoneLabel = new JLabel();
        deadZoneLabel.setHorizontalAlignment(4);
        deadZoneLabel.setHorizontalTextPosition(2);
        deadZoneLabel.setInheritsPopupMenu(false);
        deadZoneLabel.setText("Deadzone");
        deadZoneLabel.setVerifyInputWhenFocusTarget(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(deadZoneLabel, gbc);
        dzText = new JTextField();
        dzText.setHorizontalAlignment(2);
        dzText.setPreferredSize(new Dimension(65, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(dzText, gbc);
        valueLabel = new JLabel();
        valueLabel.setHorizontalAlignment(4);
        valueLabel.setPreferredSize(new Dimension(35, 17));
        valueLabel.setText("Value");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(valueLabel, gbc);
        valueText = new JTextField();
        valueText.setPreferredSize(new Dimension(65, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(valueText, gbc);
        rawLabel = new JLabel();
        rawLabel.setHorizontalAlignment(4);
        rawLabel.setMinimumSize(new Dimension(32, 17));
        rawLabel.setPreferredSize(new Dimension(32, 17));
        rawLabel.setText("Raw");
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(rawLabel, gbc);
        rawText = new JTextField();
        rawText.setPreferredSize(new Dimension(65, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(rawText, gbc);
        trimComboBox = new JComboBox();
        trimComboBox.setMaximumSize(new Dimension(100, 30));
        trimComboBox.setMinimumSize(new Dimension(100, 30));
        trimComboBox.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 11;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(trimComboBox, gbc);
        progress = new JProgressBar();
        progress.setEnabled(false);
        progress.setFocusable(false);
        progress.setMaximum(1023);
        progress.setMaximumSize(new Dimension(760, 4));
        progress.setMinimum(0);
        progress.setMinimumSize(new Dimension(760, 29));
        progress.setName("Y Axis");
        progress.setPreferredSize(new Dimension(760, 29));
        progress.setValue(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 10;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 2, 0, 0);
        mainPanel.add(progress, gbc);
        setMinButton = new JButton();
        setMinButton.setMaximumSize(new Dimension(90, 30));
        setMinButton.setMinimumSize(new Dimension(90, 30));
        setMinButton.setPreferredSize(new Dimension(90, 30));
        setMinButton.setSelected(false);
        setMinButton.setText("Set MIn");
        gbc = new GridBagConstraints();
        gbc.gridx = 11;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(setMinButton, gbc);
        setCenterButton = new JButton();
        setCenterButton.setHorizontalAlignment(0);
        setCenterButton.setHorizontalTextPosition(0);
        setCenterButton.setMaximumSize(new Dimension(90, 30));
        setCenterButton.setMinimumSize(new Dimension(90, 30));
        setCenterButton.setPreferredSize(new Dimension(90, 30));
        setCenterButton.setSelected(false);
        setCenterButton.setText("Set Center");
        gbc = new GridBagConstraints();
        gbc.gridx = 12;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(setCenterButton, gbc);
        setMaxButton = new JButton();
        setMaxButton.setHorizontalAlignment(0);
        setMaxButton.setHorizontalTextPosition(0);
        setMaxButton.setMaximumSize(new Dimension(90, 30));
        setMaxButton.setMinimumSize(new Dimension(90, 30));
        setMaxButton.setPreferredSize(new Dimension(90, 30));
        setMaxButton.setText("Set Max");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(setMaxButton, gbc);
        hasCenterCheckBox = new JCheckBox();
        hasCenterCheckBox.setHorizontalAlignment(4);
        hasCenterCheckBox.setMaximumSize(new Dimension(100, 22));
        hasCenterCheckBox.setMinimumSize(new Dimension(100, 22));
        hasCenterCheckBox.setPreferredSize(new Dimension(100, 22));
        hasCenterCheckBox.setText("Has Center");
        gbc = new GridBagConstraints();
        gbc.gridx = 12;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(hasCenterCheckBox, gbc);
        autoLimitcheckBox = new JCheckBox();
        autoLimitcheckBox.setHorizontalAlignment(4);
        autoLimitcheckBox.setMaximumSize(new Dimension(100, 22));
        autoLimitcheckBox.setMinimumSize(new Dimension(100, 22));
        autoLimitcheckBox.setPreferredSize(new Dimension(100, 22));
        autoLimitcheckBox.setText("AutoLimit");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(autoLimitcheckBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 10;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 180, 0, 0);
        mainPanel.add(spacer1, gbc);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        label1.setMaximumSize(new Dimension(40, 17));
        label1.setMinimumSize(new Dimension(40, 17));
        label1.setPreferredSize(new Dimension(40, 17));
        label1.setText("Trim");
        gbc = new GridBagConstraints();
        gbc.gridx = 10;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(label1, gbc);
        enabledCheckBox = new JCheckBox();
        enabledCheckBox.setHorizontalAlignment(4);
        enabledCheckBox.setMaximumSize(new Dimension(100, 22));
        enabledCheckBox.setMinimumSize(new Dimension(100, 22));
        enabledCheckBox.setPreferredSize(new Dimension(100, 22));
        enabledCheckBox.setText("Enabled");
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(enabledCheckBox, gbc);
        minLabel.setLabelFor(minText);
        centerLabel.setLabelFor(centerText);
        maxLabel.setLabelFor(maxText);
        deadZoneLabel.setLabelFor(dzText);
        label1.setLabelFor(trimComboBox);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}