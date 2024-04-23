package com.rkade;

import java.nio.ByteBuffer;

public final class ButtonsDataReport extends DataReport {
    private final int buttonsState;
    private final byte centerButton;
    private final int debounce;
    private final boolean multiplexShifterButtons;

    public ButtonsDataReport(byte reportType, byte reportIndex, short section, ByteBuffer buffer) {
        super(reportType, reportIndex, section);
        buttonsState = buffer.getInt();
        centerButton = buffer.get();
        debounce = Byte.toUnsignedInt(buffer.get());
        multiplexShifterButtons = buffer.get() > 0;
    }

    public int getButtonsState() {
        return buttonsState;
    }

    public byte getCenterButton() {
        return centerButton;
    }

    public int getDebounce() {
        return debounce;
    }

    public boolean isMultiplexShifterButtons() {
        return multiplexShifterButtons;
    }
}
