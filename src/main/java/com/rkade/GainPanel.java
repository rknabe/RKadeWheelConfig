package com.rkade;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Logger;

public class GainPanel implements DeviceListener, ActionListener, FocusListener, ChangeListener {
    private final static Logger logger = Logger.getLogger(GainPanel.class.getName());
    private final List<JComponent> controls;
    private JFormattedTextField gainText;
    private JLabel gainPercent;
    private JSlider gainSlider;
    private JPanel gainPanel;
    private JLabel labelText;
    private short gainIndex;
    private Device device = null;

    public GainPanel() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter() {
            public Object stringToValue(String text) throws ParseException {
                if (text == null) {
                    return null;
                } else if (text.isEmpty()) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        formatter.setFormat(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(2048);
        format.setGroupingUsed(false); //no commas
        formatter.setAllowsInvalid(false);

        DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory() {
            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                return formatter;
            }
        };
        gainText.setFormatterFactory(formatterFactory);

        controls = List.of(gainText, gainSlider);

        setPanelEnabled(false);
        setupControlListener();
    }

    public void setGainIndex(short gainIndex) {
        this.gainIndex = gainIndex;
    }

    public void setGainLabel(String label) {
        labelText.setText(label);
    }

    private void setPanelEnabled(boolean enable) {
        for (JComponent component : controls) {
            component.setEnabled(enable);
        }
    }

    private void setupControlListener() {
        for (JComponent component : controls) {
            component.addFocusListener(this);
            switch (component) {
                case AbstractButton button -> button.addActionListener(this);
                case JTextField textField -> textField.addActionListener(this);
                case JSlider slider -> slider.addChangeListener(this);
                case JComboBox<?> comboBox -> comboBox.addActionListener(this);
                default -> {
                }
            }
        }
    }

    @Override
    public void deviceAttached(Device device) {
        this.device = device;
        setPanelEnabled(true);
    }

    @Override
    public void deviceDetached(Device device) {
        this.device = null;
        setPanelEnabled(false);
    }

    @Override
    public void deviceUpdated(Device device, String status, DataReport report) {
        if (report != null) {
            if (report.getReportType() == DataReport.DATA_REPORT_ID) {
                if (report instanceof GainsDataReport gainsDataReport) {
                    updateControls(gainsDataReport);
                }
            }
        }
    }

    private void updateControls(GainsDataReport gainsDataReport) {
        short amount = gainsDataReport.getValues().get(gainIndex);
        if (!gainSlider.getValueIsAdjusting()) {
            gainSlider.setValue(amount);
            double percent = ((double) gainSlider.getValue() / (double) 1024) * 100;
            gainPercent.setText(String.format("%.1f%%", percent));
        }
        if (!gainSlider.getValueIsAdjusting() && !gainText.isFocusOwner()) {
            gainText.setValue(amount);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean status = handleGainEvent(e);
        if (!status) {
            logger.warning("Action failed for:" + e.getActionCommand());
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        boolean status = handleGainEvent(e);
        if (!status) {
            logger.warning("Focus lost, failed for:" + e.getSource());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == gainSlider) {
            if (!gainSlider.getValueIsAdjusting()) {
                device.setGainValue(gainIndex, (short) gainSlider.getValue());
            } else {
                double percent = ((double) gainSlider.getValue() / (double) 1024) * 100;
                gainPercent.setText(String.format("%.1f%%", percent));
                gainText.setValue(gainSlider.getValue());
            }
        }
    }

    private boolean handleGainEvent(AWTEvent e) {
        if (e.getSource() == gainText) {
            return device.setGainValue(gainIndex, Short.parseShort(gainText.getText()));
        }
        return true;
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
        gainPanel = new JPanel();
        gainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gainPanel.setMinimumSize(new Dimension(562, 41));
        labelText = new JLabel();
        labelText.setHorizontalAlignment(2);
        labelText.setMinimumSize(new Dimension(105, 17));
        labelText.setPreferredSize(new Dimension(105, 17));
        labelText.setText("");
        gainPanel.add(labelText);
        gainText = new JFormattedTextField();
        gainText.setFocusLostBehavior(1);
        gainText.setHorizontalAlignment(2);
        gainText.setMinimumSize(new Dimension(57, 30));
        gainText.setPreferredSize(new Dimension(57, 30));
        gainPanel.add(gainText);
        gainPercent = new JLabel();
        gainPercent.setEnabled(true);
        gainPercent.setMinimumSize(new Dimension(37, 17));
        gainPercent.setPreferredSize(new Dimension(37, 17));
        gainPercent.setText("100%");
        gainPanel.add(gainPercent);
        gainSlider = new JSlider();
        gainSlider.setMajorTickSpacing(128);
        gainSlider.setMaximum(2048);
        gainSlider.setMinimumSize(new Dimension(350, 31));
        gainSlider.setMinorTickSpacing(32);
        gainSlider.setName("");
        gainSlider.setPaintLabels(false);
        gainSlider.setPaintTicks(true);
        gainSlider.setPreferredSize(new Dimension(350, 31));
        gainSlider.setValue(1024);
        gainPanel.add(gainSlider);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return gainPanel;
    }

}
