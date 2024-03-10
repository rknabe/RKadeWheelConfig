package com.rkade;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MainForm implements DeviceListener, ActionListener {
    private final static String CENTER_BUTTON = "Set Center";
    private JPanel mainPanel;
    private JTabbedPane Inputs;
    private JPanel ffbPanel;
    private JPanel bottomPanel;
    private JComboBox<String> deviceComboBox;
    private JLabel firmwareLabel;
    private JComboBox<String> rangeComboBox;
    private JLabel rangeLabel;
    private JSlider wheelSlider;
    private JButton centerButton;
    private JTextField velocityText;
    private JTextField accText;
    private JLabel accLabel;
    private JLabel degreesLabel;
    private JPanel wheelPanel;
    private JPanel accPanel;
    private JPanel brakePanel;
    private JPanel clutchPanel;
    private JPanel axisPanel;
    private JLabel wheelIconLabel;
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
            firmwareLabel.setText(status);
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
                    wheelSlider.setValue(wheelData.getValue());
                    wheelSlider.setToolTipText(String.valueOf(wheelData.getValue()));
                    velocityText.setText(String.valueOf(wheelData.getVelocity()));
                    accText.setText(String.valueOf(wheelData.getAcceleration()));
                    degreesLabel.setText(String.format("%.1f°", wheelData.getAngle()));
                    if (Math.abs(wheelData.getAngle() - prevWheelRotation) > 1.0) {
                        wheelIconLabel.setIcon(new ImageIcon(rotate(wheelImage, wheelData.getAngle())));
                    }
                    prevWheelRotation = wheelData.getAngle();
                }
                //System.out.println(report);
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
        Inputs.setPreferredSize(new Dimension(1024, 600));
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
        firmwareLabel = new JLabel();
        firmwareLabel.setText("Version 1.2");
        bottomPanel.add(firmwareLabel);
        axisPanel = new JPanel();
        axisPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        axisPanel.setPreferredSize(new Dimension(1000, 700));
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
        wheelSlider = new JSlider();
        wheelSlider.setEnabled(false);
        wheelSlider.setMajorTickSpacing(8192);
        wheelSlider.setMaximum(32768);
        wheelSlider.setMinimum(-32768);
        wheelSlider.setPaintLabels(true);
        wheelSlider.setPaintTicks(true);
        wheelSlider.setPaintTrack(true);
        wheelSlider.setPreferredSize(new Dimension(400, 40));
        wheelSlider.setSnapToTicks(false);
        wheelSlider.setValue(0);
        wheelSlider.setValueIsAdjusting(false);
        wheelPanel.add(wheelSlider);
        wheelIconLabel = new JLabel();
        wheelIconLabel.setPreferredSize(new Dimension(40, 40));
        wheelIconLabel.setText("");
        wheelIconLabel.setVerticalAlignment(1);
        wheelPanel.add(wheelIconLabel);
        degreesLabel = new JLabel();
        degreesLabel.setHorizontalTextPosition(2);
        degreesLabel.setPreferredSize(new Dimension(50, 31));
        degreesLabel.setText("00.00°");
        wheelPanel.add(degreesLabel);
        final JLabel label1 = new JLabel();
        label1.setText("Velocity:");
        wheelPanel.add(label1);
        velocityText = new JTextField();
        velocityText.setMinimumSize(new Dimension(25, 30));
        velocityText.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(velocityText);
        accLabel = new JLabel();
        accLabel.setText("Acc:");
        wheelPanel.add(accLabel);
        accText = new JTextField();
        accText.setPreferredSize(new Dimension(65, 30));
        wheelPanel.add(accText);
        accPanel = new JPanel();
        accPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        accPanel.setPreferredSize(new Dimension(1000, 75));
        accPanel.setRequestFocusEnabled(true);
        axisPanel.add(accPanel);
        accPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Axis 1 (Y - Accelorator)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        brakePanel = new JPanel();
        brakePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        brakePanel.setPreferredSize(new Dimension(1000, 75));
        brakePanel.setRequestFocusEnabled(true);
        axisPanel.add(brakePanel);
        brakePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Axis 2 (Z - Brake)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        clutchPanel = new JPanel();
        clutchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        clutchPanel.setPreferredSize(new Dimension(1000, 75));
        clutchPanel.setRequestFocusEnabled(true);
        axisPanel.add(clutchPanel);
        clutchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Axis 3 (rX - Clutch)", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
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
