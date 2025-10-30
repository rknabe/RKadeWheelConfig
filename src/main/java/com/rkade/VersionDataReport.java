package com.rkade;

import java.nio.ByteBuffer;

public final class VersionDataReport extends DataReport {
    private final String id; //6-12 bytes
    private final String version; //12 bytes;

    public VersionDataReport(byte reportType, byte reportIndex, short section, ByteBuffer buffer) {
        super(reportType, reportIndex, section);
        id = getZString(buffer, 12);
        version = getZString(buffer, 12);
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }
}
