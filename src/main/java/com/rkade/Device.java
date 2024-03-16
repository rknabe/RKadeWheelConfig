package com.rkade;

import com.fazecast.jSerialComm.SerialPort;
import io.github.libsdl4j.api.haptic.SDL_Haptic;
import io.github.libsdl4j.api.haptic.SDL_HapticEffect;
import io.github.libsdl4j.api.joystick.SDL_Joystick;
import purejavahidapi.HidDevice;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import static com.rkade.DataReport.*;
import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.SdlSubSystemConst.*;
import static io.github.libsdl4j.api.haptic.SDL_HapticDirectionEncoding.SDL_HAPTIC_CARTESIAN;
import static io.github.libsdl4j.api.haptic.SDL_HapticEffectType.*;
import static io.github.libsdl4j.api.haptic.SdlHaptic.*;
import static io.github.libsdl4j.api.joystick.SdlJoystick.*;

public class Device {
    private static final Logger logger = Logger.getLogger(Device.class.getName());
    private static final int WAIT_AFTER_EFFECT_UPDATE = 5;
    private final String hidPath;
    private final HidDevice hidDevice;
    private String name;
    private SDL_Haptic hapticJoystick;
    private SerialPort port;
    private int sineEffectId = -1;
    private int springEffectId = -1;
    private int pullLeftEffectId = -1;
    private int pullRightEffectId = -1;
    private int rampEffectId = -1;
    private int frictionEffectId = -1;
    private int constantEffectId = -1;
    private int sawtoothUpEffectId = -1;
    private int sawtoothDownEffectId = -1;
    private int inertiaEffectId = -1;
    private int damperEffectId = -1;
    private int triangleEffectId = -1;

    public Device(HidDevice hidDevice, String path) {
        this.hidDevice = hidDevice;
        this.name = hidDevice.getHidDeviceInfo().getProductString();
        this.hidPath = path;
    }

    public synchronized boolean saveSettings() {
        return sendCommand(CMD_EESAVE);
    }

    public synchronized boolean setWheelCenter() {
        return sendCommand(CMD_CENTER);
    }

    public synchronized boolean setWheelRange(Short range) {
        return sendCommand(CMD_SET_RANGE, range);
    }

    public boolean setAxisLimits(short axisIndex, Short minValue, short maxValue) {
        return sendCommand(CMD_SET_AALIMITS, axisIndex, minValue, maxValue);
    }

    public boolean setAxisCenter(short axisIndex, short center) {
        return sendCommand(CMD_SET_AACENTER, axisIndex, center);
    }

    public boolean setAxisDeadZone(short axisIndex, short deadZone) {
        return sendCommand(CMD_SET_AADZ, axisIndex, deadZone);
    }

    public boolean setAxisAutoLimit(short axisIndex, short flag) {
        return sendCommand(CMD_SET_AAAUTOLIM, axisIndex, flag);
    }

    public synchronized boolean writeTextToPort(String text) {
        boolean isOpen = port.isOpen();
        if (!isOpen) {
            port.setBaudRate(9600);
            port.setParity(0);
            port.setNumStopBits(1);
            port.setNumDataBits(8);
            isOpen = port.openPort(500);
        }
        if (isOpen) {
            byte[] value = text.getBytes(StandardCharsets.US_ASCII);
            int ret = port.writeBytes(value, value.length);
            port.closePort();
            return (ret > 0);
        }
        return false;
    }

    public boolean doAutoCenter() {
        return writeTextToPort(CMD_AUTOCENTER_TEXT);
    }

    public void setPort(SerialPort port) {
        this.port = port;
    }

    private boolean sendCommand(byte command) {
        return sendCommand(command,  (short)0, (short) 0, (short) 0);
    }

    private boolean sendCommand(byte command, short arg1) {
        return sendCommand(command, arg1, (short) 0, (short) 0);
    }

    private boolean sendCommand(byte command, short arg1, short arg2) {
        return sendCommand(command, arg1, arg2, (byte) 0);
    }

    private boolean sendCommand(byte command, short arg1, short arg2, short arg3) {
        byte[] data = new byte[7];
        data[0] = command;
        data[1] = getFirstByte(arg1);
        data[2] = getSecondByte(arg1);

        data[3] = getFirstByte(arg2);
        data[4] = getSecondByte(arg2);

        data[5] = getFirstByte(arg3);
        data[6] = getSecondByte(arg3);

        int ret = hidDevice.setOutputReport(CMD_REPORT_ID, data, 7);
        if (ret <= 0) {
            logger.severe("Device returned error on Save:" + ret);
            return false;
        }
        return true;
    }

    private byte getFirstByte(short value) {
        return (byte) (value & 0xff);
    }

    private byte getSecondByte(short value) {
        return (byte) ((value >> 8) & 0xff);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }
    }

    private synchronized SDL_Haptic getHapticJoystick() {
        if (hapticJoystick == null) {
            int ret = SDL_Init(SDL_INIT_JOYSTICK | SDL_INIT_HAPTIC | SDL_INIT_GAMECONTROLLER);
            if (ret != 0) {
                logger.severe("Could not initialize SDL");
                return null;
            }
            int numJoysticks = SDL_NumJoysticks();
            SDL_Joystick arduinoFfb = null;
            for (int i = 0; i < numJoysticks; i++) {
                SDL_Joystick gameController = SDL_JoystickOpen(i);
                String sdlDevicePath = SDL_JoystickPath(gameController);
                if (hidPath.equals(sdlDevicePath)) {
                    arduinoFfb = gameController;
                    break;
                }
                SDL_JoystickClose(gameController);
            }
            hapticJoystick = SDL_HapticOpenFromJoystick(arduinoFfb);
        }
        return hapticJoystick;
    }

    public synchronized boolean doFfbSine() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (sineEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_SINE);
            sineEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.periodic.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.periodic.direction.dir[0] = 1;
            effect.constant.direction.dir[1] = 0; //Y Position
            effect.periodic.period = 100;
            effect.periodic.magnitude = 9000;
            effect.periodic.length = 2000;
            effect.periodic.attackLength = 120;
            effect.periodic.fadeLength = 120;
            SDL_HapticUpdateEffect(ffbDevice, sineEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, sineEffectId, 1) == 0;
    }

    public synchronized boolean doFfbSpring() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (springEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_SPRING);
            springEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.condition.delay = 0;
            effect.condition.length = 5000;
            effect.condition.direction.dir[0] = 1;
            effect.constant.direction.dir[1] = 1; //Y Position
            effect.condition.leftCoeff[0] = (short) (30000);
            effect.condition.rightCoeff[0] = (short) (30000);
            effect.condition.leftSat[0] = (short) ((30000) * 10);
            effect.condition.rightSat[0] = (short) ((30000) * 10);
            effect.condition.center[0] = 0;
            SDL_HapticUpdateEffect(ffbDevice, springEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, springEffectId, 1) == 0;
    }

    public synchronized boolean doFfbPullLeft() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (pullLeftEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_CONSTANT);
            pullLeftEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.constant.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.constant.direction.dir[0] = 1;
            effect.constant.length = 500;
            effect.constant.delay = 0;
            effect.constant.level = 8000;
            SDL_HapticUpdateEffect(ffbDevice, pullLeftEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, pullLeftEffectId, 1) == 0;
    }

    public synchronized boolean doFfbPullRight() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (pullRightEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_CONSTANT);
            pullRightEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.constant.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.constant.length = 500;
            effect.constant.delay = 0;
            effect.constant.direction.dir[0] = -1;
            effect.constant.level = -8000;
            SDL_HapticUpdateEffect(ffbDevice, pullRightEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, pullRightEffectId, 1) == 0;
    }

    public synchronized boolean doFfbRamp() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (rampEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_RAMP);
            rampEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.ramp.type = SDL_HAPTIC_RAMP;
            effect.ramp.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.ramp.delay = 0;
            effect.ramp.length = 8000;
            effect.ramp.start = 0x4000;
            effect.ramp.end = -0x4000;
            SDL_HapticUpdateEffect(ffbDevice, rampEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, rampEffectId, 1) == 0;
    }

    public synchronized boolean doFfbConstant() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (constantEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_CONSTANT);
            constantEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.constant.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.constant.direction.dir[0] = 1;
            effect.constant.length = 500;
            effect.constant.delay = 0;
            effect.constant.level = 8000;
            SDL_HapticUpdateEffect(ffbDevice, constantEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, constantEffectId, 1) == 0;
    }

    public synchronized boolean doFfbFriction() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (frictionEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_FRICTION);
            frictionEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.condition.delay = 0;
            effect.condition.length = 5000;
            effect.condition.direction.dir[0] = 1; // not used
            effect.constant.direction.dir[1] = 0; //Y Position
            effect.condition.leftSat[0] = (short) 0xFFFF;
            effect.condition.rightSat[0] = (short) 0xFFFF;
            effect.condition.leftCoeff[0] = (short) 32767.0;
            effect.condition.rightCoeff[0] = (short) 32767.0;
            SDL_HapticUpdateEffect(ffbDevice, frictionEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, frictionEffectId, 1) == 0;
    }

    public synchronized boolean doFfbSawtoothUp() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (sawtoothUpEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_SAWTOOTHUP);
            sawtoothUpEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.type = SDL_HAPTIC_SAWTOOTHUP;
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.periodic.period = 500;
            effect.periodic.magnitude = 0x5000;
            effect.periodic.length = 5000;
            effect.periodic.attackLength = 1000;
            effect.periodic.fadeLength = 1000;
            SDL_HapticUpdateEffect(ffbDevice, sawtoothUpEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, sawtoothUpEffectId, 1) == 0;
    }

    public synchronized boolean doFfbSawtoothDown() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (sawtoothDownEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_SAWTOOTHDOWN);
            sawtoothDownEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.type = SDL_HAPTIC_SAWTOOTHDOWN;
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.periodic.period = 500;
            effect.periodic.magnitude = 0x5000;
            effect.periodic.length = 5000;
            effect.periodic.attackLength = 1000;
            effect.periodic.fadeLength = 1000;
            SDL_HapticUpdateEffect(ffbDevice, sawtoothDownEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, sawtoothDownEffectId, 1) == 0;
    }

    public synchronized boolean doFfbInertia() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (inertiaEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_INERTIA);
            inertiaEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.type = SDL_HAPTIC_INERTIA;
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.condition.delay = 0;
            effect.condition.length = 5000;
            effect.condition.direction.dir[0] = 1; // not used
            effect.constant.direction.dir[1] = 0; //Y Position
            effect.condition.leftSat[0] = (short) 0xFFFF;
            effect.condition.rightSat[0] = (short) 0xFFFF;
            effect.condition.leftCoeff[0] = (short) 32767.0;
            effect.condition.rightCoeff[0] = (short) 32767.0;
            SDL_HapticUpdateEffect(ffbDevice, inertiaEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, inertiaEffectId, 1) == 0;
    }

    public synchronized boolean doFfbDamper() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (damperEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_DAMPER);
            damperEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.type = SDL_HAPTIC_DAMPER;
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.condition.delay = 0;
            effect.condition.length = 5000;
            effect.condition.direction.dir[0] = 1; // not used
            effect.constant.direction.dir[1] = 0; //Y Position
            effect.condition.leftSat[0] = (short) 0xFFFF;
            effect.condition.rightSat[0] = (short) 0xFFFF;
            effect.condition.leftCoeff[0] = (short) 32767.0;
            effect.condition.rightCoeff[0] = (short) 32767.0;
            SDL_HapticUpdateEffect(ffbDevice, damperEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, damperEffectId, 1) == 0;
    }

    public synchronized boolean doFfbTriangle() {
        SDL_Haptic ffbDevice = getHapticJoystick();
        if (triangleEffectId < 0) {
            SDL_HapticEffect effect = createEffect(SDL_HAPTIC_TRIANGLE);
            triangleEffectId = SDL_HapticNewEffect(ffbDevice, effect);
            effect.condition.type = SDL_HAPTIC_TRIANGLE;
            effect.condition.direction.type = SDL_HAPTIC_CARTESIAN;
            effect.periodic.period = 500;
            effect.periodic.magnitude = 0x5000;
            effect.periodic.length = 5000;
            effect.periodic.attackLength = 1000;
            effect.periodic.fadeLength = 1000;
            SDL_HapticUpdateEffect(ffbDevice, triangleEffectId, effect);
            //seems at least 2 milliseconds sleep needed after update
            sleep(WAIT_AFTER_EFFECT_UPDATE);
        }
        return SDL_HapticRunEffect(ffbDevice, triangleEffectId, 1) == 0;
    }

    private SDL_HapticEffect createEffect(int type) {
        SDL_HapticEffect effect = new SDL_HapticEffect();
        //cannot set this directly, or it is zeroed out by HapticNewEffect call
        effect.writeField("type", (short) type);
        return effect;
    }
}
