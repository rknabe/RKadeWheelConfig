package com.rkade;

import java.nio.ByteBuffer;

public final class ButtonsDataReport extends DataReport {
    private final int buttonsState;
    private final byte centerButton;
    private final byte debounce;

    public ButtonsDataReport(byte reportType, byte reportIndex, short section, ByteBuffer buffer) {
        super(reportType, reportIndex, section);
        buttonsState = buffer.getInt();
        centerButton = buffer.get();
        debounce = buffer.get();
    }

    public int getButtonsState() {
        return buttonsState;
    }

    public byte getCenterButton() {
        return centerButton;
    }

    public byte getDebounce() {
        return debounce;
    }

    @Override
    public String toString() {
        return "ButtonsDataReport{" +
                "buttonsState=" + buttonsState +
                ", centerButton=" + centerButton +
                ", debounce=" + debounce +
                ", reportType=" + reportType +
                ", reportIndex=" + reportIndex +
                ", section=" + section +
                '}';
    }
}
