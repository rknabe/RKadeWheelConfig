package com.rkade;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class MainForm implements DeviceListener, ActionListener, FocusListener {
    private final static Logger logger = Logger.getLogger(MainForm.class.getName());
    private final static List<String> axisLabels = List.of(
            "Axis 1 (Y - Accelerator)",
            "Axis 2 (Z - Brake)",
            "Axis 3 (rX - Clutch)",
            "Axis 4 (rY - Aux 1)",
            "Axis 5 (rZ - Aux 2)",
            "Axis 6 (Slider - Aux 3)",
            "Axis 7 (Dial - Aux 4)");
    private final static List<String> gainLabels = List.of(
            "All",
            "Constant",
            "Ramp",
            "Square",
            "Sine",
            "Triangle",
            "Sawtooth Up",
            "Sawtooth Down",
            "Spring",
            "Damper",
            "Inertia",
            "Friction");
    private final List<AxisPanel> axisPanels;
    private final List<GainPanel> gainPanels;
    private final List<JComponent> controls;
    private JPanel mainPanel;
    private JTabbedPane mainTab;
    private JPanel ffbPanel;
    private JPanel bottomPanel;
    private JComboBox<String> deviceComboBox;
    private JLabel statusLabel;
    private JComboBox<String> rangeComboBox;
    private JLabel rangeLabel;
    private JButton centerButton;
    private JTextField velocityText;
    private JTextField accText;
    private JLabel accLabel;
    private JLabel degreesLabel;
    private JPanel wheelPanel;
    private JPanel axisPanel;
    private JLabel wheelIconLabel;
    private JLabel wheelRawLabel;
    private JTextField wheelRawTextField;
    private JTextField wheelValueTextField;
    private JLabel wheelValueLabel;
    private JLabel versionLabel;
    private AxisPanel axis1Panel;
    private AxisPanel axis2Panel;
    private AxisPanel axis3Panel;
    private AxisPanel axis4Panel;
    private AxisPanel axis5Panel;
    private AxisPanel axis6Panel;
    private AxisPanel axis7Panel;
    private JScrollPane axisScroll;
    private JPanel inputsPanel;
    private JButton defaultsButton;
    private JButton loadButton;
    private JButton saveButton;
    private JButton autoCenterButton;
    private JScrollPane ffbScroll;
    private JPanel ffbSubPanel;
    private JPanel gainsPanel;
    private JPanel rightPanel;
    private JPanel miscPanel;
    private JPanel testPanel;
    private BufferedImage wheelImage;
    private double prevWheelRotation = 0.0;
    private Device device = null;

    public MainForm() {
        centerButton.addActionListener(this);
        try {
            ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("wheel55.png"));
            wheelImage = toBufferedImage(imageIcon.getImage());
            wheelIconLabel.setIcon(imageIcon);
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }
        rangeComboBox.addItem("180");
        rangeComboBox.addItem("270");
        rangeComboBox.addItem("360");
        rangeComboBox.addItem("540");
        rangeComboBox.addItem("720");
        rangeComboBox.addItem("900");
        rangeComboBox.addItem("1080");

        axisPanels = List.of(axis1Panel, axis2Panel, axis3Panel, axis4Panel, axis5Panel, axis6Panel, axis7Panel);
        gainPanels = List.of(new GainPanel(), new GainPanel(), new GainPanel(), new GainPanel(), new GainPanel(), new GainPanel(),
                new GainPanel(), new GainPanel(), new GainPanel(), new GainPanel(), new GainPanel(), new GainPanel());
        setupAxisPanels();
        setupGainPanels();

        controls = List.of(deviceComboBox, rangeComboBox, centerButton, autoCenterButton, saveButton, defaultsButton, loadButton);
        setupControlListener();
        setPanelEnabled(false);
    }

    private void setupAxisPanels() {
        for (short i = 0; i < axisPanels.size(); i++) {
            AxisPanel panel = axisPanels.get(i);
            if (panel != null) {
                panel.setAxisLabel(axisLabels.get(i));
                panel.setAxisIndex(i);
            }
        }
    }

    private void setupGainPanels() {
        for (short i = 0; i < gainPanels.size(); i++) {
            GainPanel panel = gainPanels.get(i);
            if (panel != null) {
                gainsPanel.add(panel.$$$getRootComponent$$$());
                panel.setGainLabel(gainLabels.get(i));
                panel.setGainIndex(i);
            }
        }
    }

    private void setupControlListener() {
        for (JComponent component : controls) {
            component.addFocusListener(this);
            switch (component) {
                case AbstractButton abstractButton -> abstractButton.addActionListener(this);
                case JTextField jTextField -> jTextField.addActionListener(this);
                case JComboBox<?> jComboBox -> jComboBox.addActionListener(this);
                default -> {
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (device != null && !e.getActionCommand().isEmpty()) {
            boolean status;
            if (e.getActionCommand().equals(centerButton.getActionCommand())) {
                status = device.setWheelCenter();
            } else if (e.getActionCommand().equals(rangeComboBox.getActionCommand())) {
                status = device.setWheelRange(Short.valueOf(Objects.requireNonNull(rangeComboBox.getSelectedItem()).toString()));
            } else if (e.getActionCommand().equals(autoCenterButton.getActionCommand())) {
                status = doWheelAutoCenter();
            } else if (e.getActionCommand().equals(saveButton.getActionCommand())) {
                status = device.saveSettings();
            } else {
                return;
            }
            if (!status) {
                logger.warning("Action failed for:" + e.getActionCommand());
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    private boolean doWheelAutoCenter() {
        final boolean[] status = {true};
        JLabel validator = new JLabel("<html><body>Please keep hands off wheel!<br>Press OK When AutoCentering is Complete.</body></html>");
        JOptionPane pane = new JOptionPane(validator, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION);
        final JDialog dialog = pane.createDialog(autoCenterButton, "Please keep hands off wheel!");
        dialog.setModal(true);
        SwingWorker<Void, Void> myWorker = new SwingWorker<>() {
            public Void doInBackground() {
                status[0] = device.doAutoCenter();
                return null;
            }
        };
        myWorker.execute();
        dialog.setVisible(true);
        if (status[0]) {
            return device.setWheelCenter();
        }
        return false;
    }

    @Override
    public void deviceAttached(Device device) {
        this.device = device;
        deviceComboBox.addItem(device.getName());
        setPanelEnabled(true);
        for (AxisPanel axisPanel : axisPanels) {
            axisPanel.deviceAttached(device);
        }
        for (GainPanel gainPanel : gainPanels) {
            gainPanel.deviceAttached(device);
        }
    }

    @Override
    public void deviceDetached(Device device) {
        deviceComboBox.removeItem(device.getName());
        this.device = null;
        setPanelEnabled(false);
        for (AxisPanel axisPanel : axisPanels) {
            axisPanel.deviceDetached(device);
        }
        for (GainPanel gainPanel : gainPanels) {
            gainPanel.deviceDetached(device);
        }
    }

    @Override
    public void deviceUpdated(Device device, String status, DataReport report) {
        if (status != null) {
            statusLabel.setText(status);
        }

        if (report != null) {
            if (report.getReportType() == DataReport.DATA_REPORT_ID) {
                switch (report) {
                    case WheelDataReport wheelData -> updateWheelPanel(wheelData);
                    case AxisDataReport axisData -> updateAxisPanel(axisPanels.get(axisData.getAxis() - 1), device, status, report);
                    case GainsDataReport gainsData -> updateGainPanels(device, status, gainsData);
                    case VersionDataReport versionData ->
                            versionLabel.setText(versionData.getId() + ":" + versionData.getVersion());
                    default -> {
                    }
                }
            }
        }
    }

    private void updateWheelPanel(WheelDataReport wheelData) {
        if (Math.abs(wheelData.getAngle() - prevWheelRotation) > 0.2) {
            wheelIconLabel.setIcon(new ImageIcon(rotate(wheelImage, wheelData.getAngle())));
        }
        degreesLabel.setText(String.format("%.1f°", wheelData.getAngle()));
        prevWheelRotation = wheelData.getAngle();
        if (!rangeComboBox.isFocusOwner()) {
            String newRange = String.valueOf(wheelData.getRange());
            String oldRange = (String) rangeComboBox.getSelectedItem();
            if (!newRange.equals(oldRange)) {
                rangeComboBox.setSelectedItem(newRange);
            }
        }
        wheelRawTextField.setText(String.valueOf(wheelData.getRawValue()));
        wheelValueTextField.setText(String.valueOf(wheelData.getValue()));
        velocityText.setText(String.valueOf(wheelData.getVelocity()));
        accText.setText(String.valueOf(wheelData.getAcceleration()));
    }

    private void updateGainPanels(Device device, String status, GainsDataReport report) {
        for (GainPanel gainPanel : gainPanels) {
            gainPanel.deviceUpdated(device, status, report);
        }
    }

    private void updateAxisPanel(AxisPanel axisPanel, Device device, String status, DataReport report) {
        if (axisPanel != null) {
            axisPanel.deviceUpdated(device, status, report);
        }
    }

    public JComponent getRootComponent() {
        return mainPanel;
    }

    private void setPanelEnabled(boolean isEnabled) {
        for (JComponent component : controls) {
            component.setEnabled(isEnabled);
        }
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    private BufferedImage rotate(BufferedImage image, Double degrees) {
        // Calculate the new size of the image based on the angle of rotation
        double radians = Math.toRadians(degrees);
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (image.getWidth() / 2.0), y + (image.getHeight() / 2.0));
        at.translate(x, y);
        g2d.setTransform(at);
        // Paint the original image
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotate;
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
        mainPanel.setMinimumSize(new Dimension(1060, 400));
        mainPanel.setPreferredSize(new Dimension(1060, 800));
        mainTab = new JTabbedPane();
        mainTab.setMinimumSize(new Dimension(1060, 400));
        mainTab.setName("Inputs");
        mainTab.setPreferredSize(new Dimension(1060, 800));
        mainTab.setTabLayoutPolicy(0);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(mainTab, gbc);
        inputsPanel = new JPanel();
        inputsPanel.setLayout(new BorderLayout(0, 0));
        inputsPanel.setMinimumSize(new Dimension(1060, 60));
        inputsPanel.setPreferredSize(new Dimension(1060, 800));
        mainTab.addTab("Inputs", inputsPanel);
        axisScroll = new JScrollPane();
        axisScroll.setAutoscrolls(true);
        axisScroll.setMaximumSize(new Dimension(32767, 32767));
        axisScroll.setMinimumSize(new Dimension(1060, 60));
        axisScroll.setPreferredSize(new Dimension(1060, 590));
        inputsPanel.add(axisScroll, BorderLayout.WEST);
        axisPanel = new JPanel();
        axisPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        axisPanel.setAutoscrolls(true);
        axisPanel.setMaximumSize(new Dimension(32767, 32767));
        axisPanel.setMinimumSize(new Dimension(1040, 60));
        axisPanel.setPreferredSize(new Dimension(1040, 890));
        axisScroll.setViewportView(axisPanel);
        axisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        wheelPanel = new JPanel();
        wheelPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        wheelPanel.setMinimumSize(new Dimension(1024, 82));
        wheelPanel.setPreferredSize(new Dimension(1024, 82));
        axisPanel.add(wheelPanel);
        wheelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Axis 0 (X - Steering)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        rangeLabel = new JLabel();
        rangeLabel.setText("Range");
        wheelPanel.add(rangeLabel);
        rangeComboBox = new JComboBox();
        rangeComboBox.setActionCommand("rangeChanged");
        rangeComboBox.setEditable(true);
        rangeComboBox.setMinimumSize(new Dimension(100, 30));
        rangeComboBox.setPreferredSize(new Dimension(100, 30));
        wheelPanel.add(rangeComboBox);
        centerButton = new JButton();
        centerButton.setPreferredSize(new Dimension(100, 30));
        centerButton.setText("Set Center");
        wheelPanel.add(centerButton);
        autoCenterButton = new JButton();
        autoCenterButton.setActionCommand("autoCenter");
        autoCenterButton.setText("AutoCenter");
        wheelPanel.add(autoCenterButton);
        wheelIconLabel = new JLabel();
        wheelIconLabel.setAlignmentY(0.0f);
        wheelIconLabel.setFocusable(false);
        wheelIconLabel.setPreferredSize(new Dimension(55, 55));
        wheelIconLabel.setRequestFocusEnabled(false);
        wheelIconLabel.setText("");
        wheelIconLabel.setVerticalAlignment(1);
        wheelPanel.add(wheelIconLabel);
        degreesLabel = new JLabel();
        degreesLabel.setFocusable(false);
        degreesLabel.setHorizontalTextPosition(2);
        degreesLabel.setPreferredSize(new Dimension(50, 31));
        degreesLabel.setText("00.00°");
        wheelPanel.add(degreesLabel);
        wheelRawLabel = new JLabel();
        wheelRawLabel.setFocusable(false);
        wheelRawLabel.setText("Raw");
        wheelPanel.add(wheelRawLabel);
        wheelRawTextField = new JTextField();
        wheelRawTextField.setEditable(false);
        wheelRawTextField.setFocusable(false);
        wheelRawTextField.setMinimumSize(new Dimension(25, 30));
        wheelRawTextField.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(wheelRawTextField);
        wheelValueLabel = new JLabel();
        wheelValueLabel.setFocusable(false);
        wheelValueLabel.setText("Value");
        wheelPanel.add(wheelValueLabel);
        wheelValueTextField = new JTextField();
        wheelValueTextField.setEditable(false);
        wheelValueTextField.setFocusable(false);
        wheelValueTextField.setMinimumSize(new Dimension(25, 30));
        wheelValueTextField.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(wheelValueTextField);
        final JLabel label1 = new JLabel();
        label1.setFocusable(false);
        label1.setText("Velocity");
        wheelPanel.add(label1);
        velocityText = new JTextField();
        velocityText.setEditable(false);
        velocityText.setFocusable(false);
        velocityText.setMinimumSize(new Dimension(25, 30));
        velocityText.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(velocityText);
        accLabel = new JLabel();
        accLabel.setFocusable(false);
        accLabel.setText("Acc");
        wheelPanel.add(accLabel);
        accText = new JTextField();
        accText.setEditable(false);
        accText.setFocusable(false);
        accText.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(accText);
        axis1Panel = new AxisPanel();
        axisPanel.add(axis1Panel.$$$getRootComponent$$$());
        axis2Panel = new AxisPanel();
        axisPanel.add(axis2Panel.$$$getRootComponent$$$());
        axis3Panel = new AxisPanel();
        axisPanel.add(axis3Panel.$$$getRootComponent$$$());
        axis4Panel = new AxisPanel();
        axisPanel.add(axis4Panel.$$$getRootComponent$$$());
        axis5Panel = new AxisPanel();
        axisPanel.add(axis5Panel.$$$getRootComponent$$$());
        axis6Panel = new AxisPanel();
        axisPanel.add(axis6Panel.$$$getRootComponent$$$());
        axis7Panel = new AxisPanel();
        axisPanel.add(axis7Panel.$$$getRootComponent$$$());
        ffbPanel = new JPanel();
        ffbPanel.setLayout(new BorderLayout(0, 0));
        ffbPanel.setAutoscrolls(false);
        ffbPanel.setMinimumSize(new Dimension(1060, 60));
        ffbPanel.setPreferredSize(new Dimension(1060, 800));
        mainTab.addTab("Force Feedback", ffbPanel);
        ffbScroll = new JScrollPane();
        ffbScroll.setPreferredSize(new Dimension(1000, 800));
        ffbPanel.add(ffbScroll, BorderLayout.CENTER);
        ffbSubPanel = new JPanel();
        ffbSubPanel.setLayout(new BorderLayout(0, 0));
        ffbSubPanel.setPreferredSize(new Dimension(1000, 800));
        ffbScroll.setViewportView(ffbSubPanel);
        gainsPanel = new JPanel();
        gainsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gainsPanel.setMinimumSize(new Dimension(570, 700));
        gainsPanel.setPreferredSize(new Dimension(575, 800));
        ffbSubPanel.add(gainsPanel, BorderLayout.WEST);
        gainsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Effect Gains", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(0, 0));
        rightPanel.setPreferredSize(new Dimension(500, 800));
        ffbSubPanel.add(rightPanel, BorderLayout.CENTER);
        miscPanel = new JPanel();
        miscPanel.setLayout(new GridBagLayout());
        miscPanel.setPreferredSize(new Dimension(200, 250));
        rightPanel.add(miscPanel, BorderLayout.CENTER);
        miscPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        testPanel = new JPanel();
        testPanel.setLayout(new GridBagLayout());
        testPanel.setPreferredSize(new Dimension(200, 400));
        rightPanel.add(testPanel, BorderLayout.SOUTH);
        testPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Test Effects", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottomPanel.setAutoscrolls(false);
        bottomPanel.setMinimumSize(new Dimension(1060, 75));
        bottomPanel.setPreferredSize(new Dimension(1060, 75));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(bottomPanel, gbc);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        deviceComboBox = new JComboBox();
        deviceComboBox.setEditable(false);
        deviceComboBox.setMinimumSize(new Dimension(200, 30));
        deviceComboBox.setPreferredSize(new Dimension(200, 30));
        bottomPanel.add(deviceComboBox);
        versionLabel = new JLabel();
        versionLabel.setFocusable(false);
        versionLabel.setPreferredSize(new Dimension(80, 17));
        versionLabel.setText("Version");
        bottomPanel.add(versionLabel);
        statusLabel = new JLabel();
        statusLabel.setFocusable(false);
        statusLabel.setMinimumSize(new Dimension(130, 20));
        statusLabel.setPreferredSize(new Dimension(130, 20));
        statusLabel.setRequestFocusEnabled(true);
        statusLabel.setText("Device Not Found...");
        bottomPanel.add(statusLabel);
        defaultsButton = new JButton();
        defaultsButton.setMinimumSize(new Dimension(201, 30));
        defaultsButton.setPreferredSize(new Dimension(201, 30));
        defaultsButton.setText("Reset Settings to Defaults");
        bottomPanel.add(defaultsButton);
        loadButton = new JButton();
        loadButton.setText("Load Settings From EEPROM");
        bottomPanel.add(loadButton);
        saveButton = new JButton();
        saveButton.setActionCommand("saveSettings");
        saveButton.setHorizontalAlignment(0);
        saveButton.setMinimumSize(new Dimension(201, 30));
        saveButton.setPreferredSize(new Dimension(201, 30));
        saveButton.setText("Save Settings to EEPROM");
        bottomPanel.add(saveButton);
        wheelRawLabel.setLabelFor(velocityText);
        wheelValueLabel.setLabelFor(velocityText);
        label1.setLabelFor(velocityText);
        accLabel.setLabelFor(accText);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
