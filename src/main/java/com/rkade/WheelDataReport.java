package com.rkade;

import java.util.List;

public final class WheelDataReport extends DataReport {
    private final short range;
    private final short value;
    private final short rawValue;
    private final short velocity;
    private final short acceleration;
    private final double angle;

    public WheelDataReport(byte reportType, byte reportIndex, short section, List<Short> data) {
        super(reportType, reportIndex, section, data);
        range = values.get(3);
        value = values.get(2);
        rawValue = value;
        velocity = values.get(4);
        acceleration = values.get(5);
        if (range == 0) {
            angle = 0;
        }
        else {
            angle = value / (Short.MAX_VALUE / (range / 2.0));
        }
    }

    public short getRange() {
        return range;
    }

    public short getValue() {
        return value;
    }

    public short getRawValue() {
        return rawValue;
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
