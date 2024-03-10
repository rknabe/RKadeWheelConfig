package com.rkade;

import java.util.List;

public final class AxisDataReport extends DataReport {
    private final short value;
    private final short rawValue;
    private final int axis;
    private final int center;
    private final int max;
    private final boolean enabled;
    private final boolean autoLimit;
    private final boolean hasCenter;
    private final byte trim;

    public AxisDataReport(byte reportType, byte reportIndex, short section, List<Short> data) {
        super(reportType, reportIndex, section, data);
        value = values.get(2);
        axis = section + 1;
        center = values.get(4);
        max = values.get(3);
        enabled = !getBoolean(getFirstByte(values.get(7)));
        trim = getSecondByte(values.get(7));
        autoLimit = getBoolean(getFirstByte(values.get(6)));
        hasCenter = getBoolean(getSecondByte(values.get(6)));
        rawValue = values.get(0);

        //if (axis == 1) {
        //System.out.println(this);
        //}
    }

    @Override
    public String toString() {
        return "AxisDataReport{" +
                "value=" + value +
                ", rawValue=" + rawValue +
                ", axis=" + axis +
                ", center=" + center +
                ", max=" + max +
                ", enabled=" + enabled +
                ", autoLimit=" + autoLimit +
                ", hasCenter=" + hasCenter +
                ", trim=" + trim +
                ", reportType=" + reportType +
                ", reportIndex=" + reportIndex +
                ", section=" + section +
                '}';
    }

    public short getValue() {
        return value;
    }

    public short getRawValue() {
        return rawValue;
    }

    public int getAxis() {
        return axis;
    }

    public int getCenter() {
        return center;
    }

    public int getMax() {
        return max;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
