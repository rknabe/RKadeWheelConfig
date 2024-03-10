package com.rkade;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public abstract class DataReportFactory {

    public static DataReport create(byte reportType, byte[] data) {
        if (reportType == DataReport.DATA_REPORT_ID) {
            ByteBuffer buffer = ByteBuffer.allocate(data.length).order(ByteOrder.LITTLE_ENDIAN);
            buffer.put(data);
            buffer.rewind();
            byte reportIndex = buffer.get();
            short section = buffer.getShort();
            List<Short> values = new ArrayList<>(DataReport.DATA_REPORT_VALUE_COUNT);
            while (buffer.hasRemaining()) {
                values.add(buffer.getShort());
            }
            switch (reportIndex) {
                case DataReport.CMD_GET_STEER:
                    return new WheelDataReport(reportType, reportIndex, section, values);
                case DataReport.CMD_GET_ANALOG:
                    return new AxisDataReport(reportType, reportIndex, section, values);
                case DataReport.CMD_GET_VER:
                    return new VersionDataReport(reportType, reportIndex, section, data);
            }
        }

        return null;
    }
}
