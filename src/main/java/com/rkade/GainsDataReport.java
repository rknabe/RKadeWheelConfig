package com.rkade;

import java.util.List;

public class GainsDataReport extends DataReport {

    public GainsDataReport(byte reportType, byte reportIndex, short section, List<Short> data) {
        super(reportType, reportIndex, section, data);
    }

    @Override
    public String toString() {
        return "GainsDataReport{" +
                "reportType=" + reportType +
                ", reportIndex=" + reportIndex +
                ", section=" + section +
                ", values=" + values +
                '}';
    }
}
