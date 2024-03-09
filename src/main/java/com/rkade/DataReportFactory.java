package com.rkade;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public abstract class DataReportFactory implements Serializable {

    public static DataReport create(byte reportType, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(data);
        buffer.rewind();
        if (reportType == DataReport.DATA_REPORT_ID) {
            byte reportIndex = buffer.get();
            short section = buffer.getShort();
            List<Short> values = new ArrayList<>(DataReport.DATA_REPORT_VALUE_COUNT);
            while (buffer.hasRemaining()) {
                values.add(buffer.getShort());
            }
            switch (reportIndex) {
                case DataReport.CMD_GET_STEER:
                    return new WheelDataReport(reportType, reportIndex, section, values);

            }
        }

        return null;
    }
}
