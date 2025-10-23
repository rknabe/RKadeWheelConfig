package com.rkade;

import java.nio.ByteBuffer;

public final class ButtonsDataReport extends DataReport {
    private final int buttonsState;
    private final byte shiftButton;
    private final int debounce;
    private final boolean multiplexShifterButtons;
    private ButtonAction button11Action = ButtonAction.UNKNOWN;
    private ButtonAction button12Action = ButtonAction.UNKNOWN;
    private ButtonAction button13Action = ButtonAction.UNKNOWN;
    private ButtonAction button14Action = ButtonAction.UNKNOWN;

    public ButtonsDataReport(byte reportType, byte reportIndex, short section, ByteBuffer buffer) {
        super(reportType, reportIndex, section);
        buttonsState = buffer.getInt();
        shiftButton = buffer.get();
        debounce = Byte.toUnsignedInt(buffer.get());
        multiplexShifterButtons = buffer.get() > 0;
        if (buffer.hasRemaining()) {
            button11Action = ButtonAction.from(buffer.get());
            button12Action = ButtonAction.from(buffer.get());
            button13Action = ButtonAction.from(buffer.get());
            button14Action = ButtonAction.from(buffer.get());
        }
    }

    public int getButtonsState() {
        return buttonsState;
    }

    public byte getShiftButton() {
        return shiftButton;
    }

    public int getDebounce() {
        return debounce;
    }

    public boolean isMultiplexShifterButtons() {
        return multiplexShifterButtons;
    }

    public ButtonAction getButton11Action() {
        return button11Action;
    }

    public ButtonAction getButton12Action() {
        return button12Action;
    }

    public ButtonAction getButton13Action() {
        return button13Action;
    }

    public ButtonAction getButton14Action() {
        return button14Action;
    }
}
