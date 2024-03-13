package com.rkade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class App {
    private final static Logger logger = LogManager.getLogger(App.class);
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
                logger.error(ex);
            }
        });
        /*
        import io.github.libsdl4j.api.haptic.SDL_Haptic;
        import io.github.libsdl4j.api.haptic.SDL_HapticEffect;
        import io.github.libsdl4j.api.joystick.SDL_Joystick;
        import io.github.libsdl4j.api.joystick.SDL_JoystickGUID;
        import static io.github.libsdl4j.api.Sdl.SDL_Init;
        import static io.github.libsdl4j.api.SdlSubSystemConst.*;
        import static io.github.libsdl4j.api.event.SdlEventsConst.SDL_ENABLE;
        import static io.github.libsdl4j.api.haptic.SDL_HapticDirectionEncoding.SDL_HAPTIC_CARTESIAN;
        import static io.github.libsdl4j.api.haptic.SDL_HapticEffectType.SDL_HAPTIC_SINE;
        import static io.github.libsdl4j.api.haptic.SdlHaptic.*;
        import static io.github.libsdl4j.api.hints.SdlHints.SDL_SetHint;
        import static io.github.libsdl4j.api.hints.SdlHintsConst.SDL_HINT_JOYSTICK_RAWINPUT;
        import static io.github.libsdl4j.api.joystick.SdlJoystick.*;
        //USB\VID_2341&PID_8036&MI_02\6&994C2E2&0&0002
        SDL_SetHint(SDL_HINT_JOYSTICK_RAWINPUT, "0");
        int ret = SDL_Init(SDL_INIT_JOYSTICK | SDL_INIT_HAPTIC | SDL_INIT_GAMECONTROLLER);
        ret = SDL_JoystickEventState(SDL_ENABLE);
        SDL_JoystickUpdate();
        int numJoysticks = SDL_NumJoysticks();
        SDL_Joystick gameController = SDL_JoystickOpen(0);
        String name =  SDL_JoystickName(gameController);
        SDL_JoystickGUID guid = guid = SDL_JoystickGetGUID(gameController);
        SDL_Haptic hapticJoystick = SDL_HapticOpenFromJoystick(gameController);
        SDL_HapticEffect tempEffect = new SDL_HapticEffect();
        tempEffect.setType(SDL_HAPTIC_SINE);
        tempEffect.periodic.direction.type = SDL_HAPTIC_CARTESIAN;
        tempEffect.periodic.direction.dir[0] = 1;
        tempEffect.constant.direction.dir[1] = 0; //Y Position
        tempEffect.periodic.period = 100;
        tempEffect.periodic.magnitude = 9000;
        tempEffect.periodic.length = 2000;
        tempEffect.periodic.attackLength = 120;
        tempEffect.periodic.fadeLength = 120;
        int effect_id = SDL_HapticNewEffect(hapticJoystick, tempEffect);
        SDL_HapticRunEffect(hapticJoystick, effect_id, 1);*/
    }
}
