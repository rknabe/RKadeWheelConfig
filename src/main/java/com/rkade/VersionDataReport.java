package com.rkade;

public class VersionDataReport extends DataReport {
    private final String id; //6 bytes
    private final String version; //12 bytes;

    public VersionDataReport(byte reportType, byte reportIndex, short section, byte[] data) {
        super(reportType, reportIndex, section, data);

        id = substring(data, 3, 9);
        version = substring(data, 9, 21);
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }
}
