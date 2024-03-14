package com.rkade;

import io.github.libsdl4j.api.haptic.SDL_Haptic;
import io.github.libsdl4j.api.haptic.SDL_HapticEffect;
import io.github.libsdl4j.api.joystick.SDL_Joystick;
import io.github.libsdl4j.api.joystick.SDL_JoystickGUID;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.SdlSubSystemConst.*;
import static io.github.libsdl4j.api.event.SdlEventsConst.SDL_ENABLE;
import static io.github.libsdl4j.api.haptic.SDL_HapticDirectionEncoding.SDL_HAPTIC_CARTESIAN;
import static io.github.libsdl4j.api.haptic.SDL_HapticEffectType.SDL_HAPTIC_SINE;
import static io.github.libsdl4j.api.haptic.SdlHaptic.*;
import static io.github.libsdl4j.api.hints.SdlHints.SDL_SetHint;
import static io.github.libsdl4j.api.hints.SdlHintsConst.SDL_HINT_JOYSTICK_RAWINPUT;
import static io.github.libsdl4j.api.joystick.SdlJoystick.*;

public class App {
    private final static Logger logger = Logger.getLogger(App.class.getName());
    private static DeviceManager deviceManager;

    public static void main(String[] args) {

        try {
            InputStream is = App.class.getResourceAsStream("/logging.properties");
            LogManager.getLogManager().readConfiguration(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
                logger.warning(ex.getMessage());
            }
        });

        //USB\VID_2341&PID_8036&MI_02\6&994C2E2&0&0002
        SDL_SetHint(SDL_HINT_JOYSTICK_RAWINPUT, "0");
        int ret = SDL_Init(SDL_INIT_JOYSTICK | SDL_INIT_HAPTIC | SDL_INIT_GAMECONTROLLER);
        ret = SDL_JoystickEventState(SDL_ENABLE);
        SDL_JoystickUpdate();
        int numJoysticks = SDL_NumJoysticks();
        SDL_Joystick arduinoFfb = null;
        int arduinoFfbIndex = -1;
        for (int i = 0; i < numJoysticks; i++) {
            SDL_Joystick gameController = SDL_JoystickOpen(i);
            if ("Arduino Leonardo".equalsIgnoreCase(SDL_JoystickName(gameController))) {
                arduinoFfbIndex = i;
                arduinoFfb = gameController;
                String path = SDL_JoystickPath(arduinoFfb);
                break;
            }
            SDL_JoystickClose(gameController);
        }
        SDL_JoystickGUID guid = guid = SDL_JoystickGetGUID(arduinoFfb);
        SDL_Haptic hapticJoystick = SDL_HapticOpenFromJoystick(arduinoFfb);
        SDL_HapticEffect tempEffect = new SDL_HapticEffect();
        //cannot set this directly, or it is zeroed out by HapticNewEffect call
        tempEffect.writeField("type", (short) SDL_HAPTIC_SINE);
        int effect_id = SDL_HapticNewEffect(hapticJoystick, tempEffect);
        tempEffect.periodic.direction.type = SDL_HAPTIC_CARTESIAN;
        tempEffect.periodic.direction.dir[0] = 1;
        tempEffect.constant.direction.dir[1] = 0; //Y Position
        tempEffect.periodic.period = 100;
        tempEffect.periodic.magnitude = 9000;
        tempEffect.periodic.length = 2000;
        tempEffect.periodic.attackLength = 120;
        tempEffect.periodic.fadeLength = 120;

        effect_id = SDL_HapticUpdateEffect(hapticJoystick, effect_id, tempEffect);
        //seems at least 2 seconds sleep needed after update
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ret = SDL_HapticRunEffect(hapticJoystick, effect_id, 1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
