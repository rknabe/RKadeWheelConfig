package com.rkade;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class DataReport implements Serializable {
    public final static byte CMD_REPORT_ID = 15;
    public final static byte DATA_REPORT_ID = 16;
    public final static byte DATA_REPORT_VALUE_COUNT = 14;
    public final static int CMD_GET_VER = 1;
    public final static int CMD_GET_STEER = 2;
    public final static int CMD_GET_ANALOG = 3;
    public final static int CMD_GET_BUTTONS = 4;
    public final static int CMD_GET_GAINS = 5;
    public final static int CMD_GET_MISC = 6;
    public final static int CMD_SET_RANGE = 10;
    public final static int CMD_SET_AALIMITS = 11;
    public final static int CMD_SET_AACENTER = 12;
    public final static int CMD_SET_AADZ = 13;
    public final static int CMD_SET_AAAUTOLIM = 14;
    public final static int CMD_SET_CENTERBTN = 15;
    public final static int CMD_SET_DEBOUNCE = 16;
    public final static int CMD_SET_GAIN = 17;
    public final static int CMD_SET_MISC = 18;
    public final static int MISC_MAXVD = 0;
    public final static int MISC_MAXVF = 1;
    public final static int MISC_MAXACC = 2;
    public final static int MISC_MINF = 3;
    public final static int MISC_MAXF = 4;
    public final static int MISC_CUTF = 5;
    public final static int MISC_FFBBD = 6;
    public final static int MISC_ENDSTOP = 7;
    public final static int CMD_SET_ODTRIM = 19;
    public final static int CMD_EELOAD = 20;
    public final static byte CMD_EESAVE = 21;
    public final static int CMD_DEFAULT = 22;
    public final static int CMD_CENTER = 23;
    private final byte reportType;
    private final short reportIndex;
    private final short section;
    private final byte[] data;
    private final List<Short> values = new ArrayList<>(DATA_REPORT_VALUE_COUNT);

    public DataReport(byte reportType, byte[] data) {
        this.reportType = reportType;
        this.data = data;
        ByteBuffer buffer = ByteBuffer.allocate(data.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(data);
        buffer.rewind();
        this.reportIndex = buffer.get();
        if (reportType == DATA_REPORT_ID) {
            this.section = buffer.getShort();
            while (buffer.hasRemaining()) {
                values.add(buffer.getShort());
            }
        } else {
            this.section = 0;
        }
        System.out.println(this);
    }

    public static short toShort(byte hi, byte lo) {
        return (short) (hi << 8 | lo & 0xFF);
    }

    public short getSection() {
        return section;
    }

    public List<Short> getValues() {
        return values;
    }

    public byte getReportType() {
        return reportType;
    }

    public short getReportIndex() {
        return reportIndex;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(String.format("DataReport{reportType=%d, reportIndex=%d, section=%d, ", reportType, reportIndex, section));
        for (short value : values) {
            s.append(String.format("%6d ", value));
        }
        return s.toString();
    }
}
