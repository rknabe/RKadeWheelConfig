package com.rkade;

import java.util.List;

public final class WheelDataReport extends DataReport {
    private final int rawValue;
    private final short value;
    private final short range;
    private final short velocity;
    private final short acceleration;
    private final double angle;

    public WheelDataReport(byte reportType, byte reportIndex, short section, List<Short> data) {
        super(reportType, reportIndex, section, data);

        rawValue = intFromShorts(values.get(0), values.get(1));
        value = values.get(2);
        range = values.get(3);
        velocity = values.get(4);
        acceleration = values.get(5);
        if (range == 0) {
            angle = 0;
        } else {
            angle = value / ((double) Short.MAX_VALUE / (range / 2.0));
        }
    }

    @Override
    public String toString() {
        return "WheelDataReport{" +
                "rawValue=" + rawValue +
                ", value=" + value +
                ", range=" + range +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", angle=" + angle +
                '}';
    }

    public int getRawValue() {
        return rawValue;
    }

    public short getValue() {
        return value;
    }

    public short getRange() {
        return range;
    }

    public short getVelocity() {
        return velocity;
    }

    public short getAcceleration() {
        return acceleration;
    }

    public double getAngle() {
        return angle;
    }
}
