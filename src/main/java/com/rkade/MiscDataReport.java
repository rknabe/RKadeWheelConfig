package com.rkade;

import java.nio.ByteBuffer;

public final class MiscDataReport extends DataReport {
    private final short maxVd;
    private final short maxVf;
    private final short maxAcc;
    private final short minForce;
    private final short maxForce;
    private final short cutForce;
    private final byte ffbBitDepth;

    public MiscDataReport(byte reportType, byte reportIndex, short section, ByteBuffer buffer) {
        super(reportType, reportIndex, section);

        maxVd = buffer.getShort();
        maxVf = buffer.getShort();
        maxAcc = buffer.getShort();
        minForce = buffer.getShort();
        maxForce = buffer.getShort();
        cutForce = buffer.getShort();
        ffbBitDepth = buffer.get();
    }

    public short getMaxVd() {
        return maxVd;
    }

    public short getMaxVf() {
        return maxVf;
    }

    public short getMaxAcc() {
        return maxAcc;
    }

    public short getMinForce() {
        return minForce;
    }

    public short getMaxForce() {
        return maxForce;
    }

    public short getCutForce() {
        return cutForce;
    }

    public byte getFfbBitDepth() {
        return ffbBitDepth;
    }
}
