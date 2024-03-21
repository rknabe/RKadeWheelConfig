package com.rkade;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DataReport {
    public final static byte CMD_REPORT_ID = 15;
    public final static byte DATA_REPORT_ID = 16;
    public final static byte DATA_REPORT_VALUE_COUNT = 31;
    public final static byte CMD_GET_VER = 1;
    public final static byte CMD_GET_STEER = 2;
    public final static byte CMD_GET_ANALOG = 3;
    public final static byte CMD_GET_BUTTONS = 4;
    public final static byte CMD_GET_GAINS = 5;
    public final static byte CMD_GET_MISC = 6;
    public final static byte CMD_SET_RANGE = 10;
    public final static byte CMD_SET_AALIMITS = 11;
    public final static byte CMD_SET_AACENTER = 12;
    public final static byte CMD_SET_AADZ = 13;
    public final static byte CMD_SET_AAAUTOLIM = 14;
    public final static byte CMD_SET_CENTERBTN = 15;
    public final static byte CMD_SET_DEBOUNCE = 16;
    public final static byte CMD_SET_GAIN = 17;
    public final static byte CMD_SET_MISC = 18;
    public final static byte MISC_MAXVD = 0;
    public final static byte MISC_MAXVF = 1;
    public final static byte MISC_MAXACC = 2;
    public final static byte MISC_MINF = 3;
    public final static byte MISC_MAXF = 4;
    public final static byte MISC_CUTF = 5;
    public final static byte MISC_FFBBD = 6;
    public final static byte MISC_ENDSTOP = 7;
    public final static byte CMD_SET_ODTRIM = 19;
    public final static byte CMD_EELOAD = 20;
    public final static byte CMD_EESAVE = 21;
    public final static byte CMD_DEFAULT = 22;
    public final static byte CMD_CENTER = 23;
    public final static String CMD_AUTOCENTER_TEXT = "autocenter ";
    public final static String CMD_SPRING_ON_TEXT = "spring 1 ";
    public final static String CMD_SPRING_OFF_TEXT = "spring 0 ";
    protected final byte reportType;
    protected final short reportIndex;
    protected final short section;
    protected final List<Short> values;
    protected final byte[] data;

    public DataReport(byte reportType, byte reportIndex, short section, List<Short> values) {
        this.reportType = reportType;
        this.reportIndex = reportIndex;
        this.section = section;
        this.values = values;
        this.data = null;
    }

    public DataReport(byte reportType, byte reportIndex, short section, byte[] data) {
        this.reportType = reportType;
        this.reportIndex = reportIndex;
        this.section = section;
        this.values = null;
        this.data = data;
    }

    protected boolean getBooleanByIndex(int index) {
        return values.get(index) == 1;
    }

    protected boolean getBoolean(int value) {
        return value == 1;
    }

    protected byte getFirstByte(short value) {
        return (byte) (value & 0xff);
    }

    protected byte getSecondByte(short value) {
        return (byte) ((value >> 8) & 0xff);
    }

    protected int intFromShorts(short low, short high) {
        return (high << 16) | (low & 0xFFFF);
    }

    protected String substring(byte[] array, int start, int end) {
        if (end <= start)
            return null;
        int length = (end - start);

        byte[] newArray = new byte[length];
        System.arraycopy(array, start, newArray, 0, length);
        return new String(newArray, StandardCharsets.ISO_8859_1);
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
}
