package com.rkade;

import org.apache.commons.cli.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {
    public static final String CL_PARAM_SPRING_ON = "springon";
    public static final String CL_PARAM_SPRING_OFF = "springoff";
    public static final String CL_PARAM_AUTO_CENTER = "autocenter";
    public static final String CL_PARAM_CENTER = "center";
    private static final Logger logger = Logger.getLogger(App.class.getName());
    private static DeviceManager deviceManager;

    public static void main(String[] args) {
        boolean showGui = true;

        try {
            InputStream is = App.class.getResourceAsStream("/logging.properties");
            LogManager.getLogManager().readConfiguration(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Options options = setupCommandLineOptions();

            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(options, args);

            if (cl.hasOption(CL_PARAM_SPRING_ON) || cl.hasOption(CL_PARAM_SPRING_OFF)
                    || cl.hasOption(CL_PARAM_AUTO_CENTER) || cl.hasOption(CL_PARAM_CENTER)) {
                showGui = false;
                Device device = DeviceManager.openDevice();
                if (device != null) {
                    if (cl.hasOption(CL_PARAM_SPRING_ON)) {
                        if (device.setConstantSpring(true)) {
                            logger.info("Constant Spring enabled");
                        } else {
                            logger.info("Error enabling Constant Spring");
                        }
                    }
                    if (cl.hasOption(CL_PARAM_SPRING_OFF)) {
                        if (device.setConstantSpring(false)) {
                            logger.info("Constant Spring disabled");
                        } else {
                            logger.info("Error disabling Constant Spring");
                        }
                    }
                    if (cl.hasOption(CL_PARAM_AUTO_CENTER)) {
                        if (device.runAutoCenter()) {
                            logger.info("AutoCenter complete");
                        } else {
                            logger.info("Error running AutoCenter");
                        }
                    }
                    if (cl.hasOption(CL_PARAM_AUTO_CENTER)) {
                        if (device.setWheelCenter()) {
                            logger.info("Wheel center set to current position");
                        } else {
                            logger.info("Error setting wheel center");
                        }
                    }
                } else {
                    logger.severe("Could not open device for cli");
                }
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        if (showGui) {
            SwingUtilities.invokeLater(() -> {
                try {
                    //com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme.setup();
                    //com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme.setup();
                    com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme.setup();
                    JFrame frame = new JFrame("RKADE Wheel Config");
                    MainForm mainForm = new MainForm();
                    frame.setContentPane(mainForm.getRootComponent());
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    Image icon = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("wheel32.png"));
                    frame.setIconImage(icon);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);

                    deviceManager = new DeviceManager();
                    deviceManager.addDeviceListener(mainForm);
                } catch (Exception ex) {
                    logger.warning(ex.getMessage());
                }
            });
        }
    }

    @NotNull
    private static Options setupCommandLineOptions() {
        Options options = new Options();

        Option springOnOption = new Option(CL_PARAM_SPRING_ON, "Turn on constant spring effect");
        springOnOption.setRequired(false);
        springOnOption.setOptionalArg(false);
        options.addOption(springOnOption);

        Option springOffOption = new Option(CL_PARAM_SPRING_OFF, "Turn off constant spring effect");
        springOffOption.setRequired(false);
        springOffOption.setOptionalArg(false);
        options.addOption(springOffOption);

        Option autocenterOption = new Option(CL_PARAM_AUTO_CENTER, "Perform automatic range and center calibration");
        autocenterOption.setRequired(false);
        autocenterOption.setOptionalArg(false);
        options.addOption(autocenterOption);

        Option centerOption = new Option(CL_PARAM_CENTER, "Set wheel center to current position");
        centerOption.setRequired(false);
        centerOption.setOptionalArg(false);
        options.addOption(centerOption);
        return options;
    }
}
