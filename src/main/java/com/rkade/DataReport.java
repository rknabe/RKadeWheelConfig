package com.rkade;

import java.io.Serializable;
import java.util.Arrays;

public class DataReport implements Serializable {
    public final static byte CMD_REPORT_ID = 15;
    public final static byte DATA_REPORT_ID = 16;
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

    public DataReport(byte reportType, byte[] data) {
        this.reportType = reportType;
        this.reportIndex = data[0];
        if (reportType == DATA_REPORT_ID) {
            this.section = toShort(data[2], data[1]);
            this.data = Arrays.copyOfRange(data, 3, data.length);
        } else {
            this.section = 0;
            this.data = Arrays.copyOfRange(data, 1, data.length);
        }
    }

    public static short toShort(byte hi, byte lo) {
        return (short) (hi << 8 | lo & 0xFF);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(String.format("DataReport{reportType=%d, reportIndex=%d, section=%d, ", reportType, reportIndex, section));
        for (byte datum : data) {
            s.append(String.format("%02X ", datum));
        }
        return s.toString();
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
}
