package com.rkade;

import java.io.Serializable;
import java.util.Arrays;

public class DataReport implements Serializable {
    private byte reportType;
    private short reportIndex;
    private byte[] data;

    public DataReport(byte reportType, byte[] data) {
        this.reportType = reportType;
        this.reportIndex = data[0];

        this.data = Arrays.copyOfRange(data, 1, data.length);
    }

    public byte getReportType() {
        return reportType;
    }

    public void setReportType(byte reportType) {
        this.reportType = reportType;
    }

    public short getReportIndex() {
        return reportIndex;
    }

    public void setReportIndex(short reportIndex) {
        this.reportIndex = reportIndex;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
