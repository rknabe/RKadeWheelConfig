package com.rkade;

import java.nio.ByteBuffer;

public final class WheelDataReport extends DataReport {
    private final int rawValue;
    private final short value;
    private final short range;
    private final short velocity;
    private final short acceleration;
    private final double angle;

    public WheelDataReport(byte reportType, byte reportIndex, short section, ByteBuffer buffer) {
        super(reportType, reportIndex, section);

        rawValue = buffer.getInt();
        value = buffer.getShort();
        range = buffer.getShort();
        velocity = buffer.getShort();
        acceleration = buffer.getShort();
        if (range == 0) {
            angle = 0;
        } else {
            angle = value / ((double) Short.MAX_VALUE / (range / 2.0));
        }
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
