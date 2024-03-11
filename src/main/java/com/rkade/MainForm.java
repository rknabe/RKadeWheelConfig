package com.rkade;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainForm implements DeviceListener, ActionListener {
    private final static String CENTER_BUTTON = "Set Center";
    private JPanel mainPanel;
    private JTabbedPane Inputs;
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
    private AxisPanel xAxisPanel;
    private BufferedImage wheelImage;
    private double prevWheelRotation = 0.0;

    public MainForm() {
        centerButton.addActionListener(this);
        try {
            ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("wheel40.png"));
            wheelImage = toBufferedImage(imageIcon.getImage());
            wheelIconLabel.setIcon(imageIcon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        TitledBorder border = (TitledBorder) xAxisPanel.getMainPanel().getBorder();
        border.setTitle("Axis 1 (Y - Accelerator)");
    }

    private Serializable clone(Serializable c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Serializable) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (CENTER_BUTTON.equals(e.getActionCommand())) {

        }
    }

    @Override
    public void deviceAttached(Device device) {
        deviceComboBox.addItem(device.getName());
    }

    @Override
    public void deviceDetached(Device device) {
        deviceComboBox.removeItem(device.getName());
    }

    @Override
    public void deviceUpdated(Device device, String status, DataReport report) {
        if (status != null) {
            statusLabel.setText(status);
        }

        if (report != null) {
            if (report.getReportType() == DataReport.DATA_REPORT_ID) {
                if (report instanceof WheelDataReport) {
                    WheelDataReport wheelData = (WheelDataReport) report;
                    String newRange = String.valueOf(wheelData.getRange());
                    String oldRange = (String) rangeComboBox.getSelectedItem();
                    if (!newRange.equals(oldRange)) {
                        rangeComboBox.removeAllItems();
                        rangeComboBox.addItem(newRange);
                    }
                    wheelRawTextField.setText(String.valueOf(wheelData.getRawValue()));
                    wheelValueTextField.setText(String.valueOf(wheelData.getValue()));
                    velocityText.setText(String.valueOf(wheelData.getVelocity()));
                    accText.setText(String.valueOf(wheelData.getAcceleration()));
                    if (Math.abs(wheelData.getAngle() - prevWheelRotation) > 0.5) {
                        wheelIconLabel.setIcon(new ImageIcon(rotate(wheelImage, wheelData.getAngle())));
                    }
                    degreesLabel.setText(String.format("%.1f°", wheelData.getAngle()));
                    prevWheelRotation = wheelData.getAngle();
                } else if (report instanceof AxisDataReport) {
                    AxisDataReport axisData = (AxisDataReport) report;
                    switch (axisData.getAxis()) {
                        case 1:
                            xAxisPanel.getProgress().setValue(axisData.getRawValue());
                            xAxisPanel.getValueText().setText(String.valueOf(axisData.getValue()));
                            xAxisPanel.getRawText().setText(String.valueOf(axisData.getRawValue()));
                            xAxisPanel.getMinText().setText(String.valueOf(axisData.getMin()));
                            xAxisPanel.getMaxText().setText(String.valueOf(axisData.getMax()));
                            xAxisPanel.getCenterText().setText(String.valueOf(axisData.getCenter()));
                            xAxisPanel.getDzText().setText(String.valueOf(axisData.getDeadZone()));
                            break;
                    }
                } else if (report instanceof VersionDataReport) {
                    VersionDataReport versionData = (VersionDataReport) report;
                    versionLabel.setText(versionData.getId() + ":" + versionData.getVersion());
                }
            }
        }
    }

    public JComponent getRootComponent() {
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
        mainPanel.setPreferredSize(new Dimension(1024, 768));
        Inputs = new JTabbedPane();
        Inputs.setPreferredSize(new Dimension(900, 600));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(Inputs, gbc);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setMinimumSize(new Dimension(1024, 600));
        panel1.setPreferredSize(new Dimension(1000, 700));
        Inputs.addTab("Inputs", panel1);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        bottomPanel.setPreferredSize(new Dimension(1000, 75));
        panel1.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        deviceComboBox = new JComboBox();
        deviceComboBox.setMinimumSize(new Dimension(200, 30));
        deviceComboBox.setPreferredSize(new Dimension(200, 30));
        bottomPanel.add(deviceComboBox);
        versionLabel = new JLabel();
        versionLabel.setPreferredSize(new Dimension(80, 17));
        versionLabel.setText("Version");
        bottomPanel.add(versionLabel);
        statusLabel = new JLabel();
        statusLabel.setPreferredSize(new Dimension(120, 17));
        statusLabel.setText("Device Not Found");
        bottomPanel.add(statusLabel);
        axisPanel = new JPanel();
        axisPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        axisPanel.setPreferredSize(new Dimension(900, 700));
        panel1.add(axisPanel, BorderLayout.NORTH);
        wheelPanel = new JPanel();
        wheelPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        wheelPanel.setMinimumSize(new Dimension(800, 82));
        wheelPanel.setPreferredSize(new Dimension(1020, 75));
        axisPanel.add(wheelPanel);
        wheelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Axis 0 (X - Steering)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        rangeLabel = new JLabel();
        rangeLabel.setText("Range");
        wheelPanel.add(rangeLabel);
        rangeComboBox = new JComboBox();
        wheelPanel.add(rangeComboBox);
        centerButton = new JButton();
        centerButton.setLabel("Set Center");
        centerButton.setPreferredSize(new Dimension(100, 30));
        centerButton.setText("Set Center");
        wheelPanel.add(centerButton);
        wheelIconLabel = new JLabel();
        wheelIconLabel.setPreferredSize(new Dimension(45, 45));
        wheelIconLabel.setRequestFocusEnabled(false);
        wheelIconLabel.setText("");
        wheelIconLabel.setVerticalAlignment(1);
        wheelPanel.add(wheelIconLabel);
        degreesLabel = new JLabel();
        degreesLabel.setHorizontalTextPosition(2);
        degreesLabel.setPreferredSize(new Dimension(50, 31));
        degreesLabel.setText("00.00°");
        wheelPanel.add(degreesLabel);
        wheelRawLabel = new JLabel();
        wheelRawLabel.setText("Raw");
        wheelPanel.add(wheelRawLabel);
        wheelRawTextField = new JTextField();
        wheelRawTextField.setMinimumSize(new Dimension(25, 30));
        wheelRawTextField.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(wheelRawTextField);
        wheelValueLabel = new JLabel();
        wheelValueLabel.setText("Value");
        wheelPanel.add(wheelValueLabel);
        wheelValueTextField = new JTextField();
        wheelValueTextField.setMinimumSize(new Dimension(25, 30));
        wheelValueTextField.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(wheelValueTextField);
        final JLabel label1 = new JLabel();
        label1.setText("Velocity");
        wheelPanel.add(label1);
        velocityText = new JTextField();
        velocityText.setMinimumSize(new Dimension(25, 30));
        velocityText.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(velocityText);
        accLabel = new JLabel();
        accLabel.setText("Acc");
        wheelPanel.add(accLabel);
        accText = new JTextField();
        accText.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(accText);
        xAxisPanel = new AxisPanel();
        axisPanel.add(xAxisPanel.$$$getRootComponent$$$());
        ffbPanel = new JPanel();
        ffbPanel.setLayout(new GridBagLayout());
        Inputs.addTab("Force Feedback", ffbPanel);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        ffbPanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        ffbPanel.add(spacer2, gbc);
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
