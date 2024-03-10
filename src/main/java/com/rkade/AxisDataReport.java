package com.rkade;

import java.util.List;

public final class AxisDataReport extends DataReport {
    private final short rawValue;
    private final short value;
    private final short min;
    private final short max;
    private final short center;
    private final short deadZone;
    private final boolean autoLimit;
    private final boolean hasCenter;
    private final boolean enabled;
    private final byte trim;
    private final int axis;

    public AxisDataReport(byte reportType, byte reportIndex, short section, List<Short> data) {
        super(reportType, reportIndex, section, data);
        axis = section + 1;
        rawValue = values.get(0);
        value = values.get(1);
        min = values.get(2);
        max = values.get(3);
        center = values.get(4);
        deadZone = values.get(5);
        autoLimit = getBoolean(getFirstByte(values.get(6)));
        hasCenter = getBoolean(getSecondByte(values.get(6)));
        enabled = !getBoolean(getFirstByte(values.get(7)));
        trim = getSecondByte(values.get(7));
    }

    @Override
    public String toString() {
        return "AxisDataReport{" +
                "axis=" + axis +
                ", rawValue=" + rawValue +
                ", value=" + value +
                ", min=" + min +
                ", max=" + max +
                ", center=" + center +
                ", deadZone=" + deadZone +
                ", autoLimit=" + autoLimit +
                ", hasCenter=" + hasCenter +
                ", enabled=" + enabled +
                ", trim=" + trim +
                '}';
    }

    public int getAxis() {
        return axis;
    }

    public short getRawValue() {
        return rawValue;
    }

    public short getValue() {
        return value;
    }

    public short getMin() {
        return min;
    }

    public short getMax() {
        return max;
    }

    public short getCenter() {
        return center;
    }

    public short getDeadZone() {
        return deadZone;
    }

    public boolean isAutoLimit() {
        return autoLimit;
    }

    public boolean isHasCenter() {
        return hasCenter;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public byte getTrim() {
        return trim;
    }
}
