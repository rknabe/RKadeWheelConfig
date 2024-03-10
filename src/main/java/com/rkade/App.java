package com.rkade;

import javax.swing.*;
import java.awt.*;

public class App {
    private static DeviceManager deviceManager;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                //com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme.setup();
                //com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme.setup();
                com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme.setup();
                JFrame frame = new JFrame("Wheel Config");
                MainForm mainForm = new MainForm();
                frame.setContentPane(mainForm.getRootComponent());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Image icon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("wheel.png"));
                frame.setIconImage(icon);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                deviceManager = new DeviceManager();
                deviceManager.addDeviceListener(mainForm);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
