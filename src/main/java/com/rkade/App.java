package com.rkade;

import purejavahidapi.*;

import java.util.List;

public class App {
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
    volatile static boolean deviceOpen = false;
    private static byte currentDataType = CMD_GET_VER;
    private static byte currentDataIndex = 0;

    public static void main(String[] args) {
        try {

            while (true) {
                // System.exit(0);
                HidDeviceInfo devInfo = null;
                if (!deviceOpen) {
                    System.out.println("scanning");
                    List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
                    for (HidDeviceInfo info : devList) {
                        // if (info.getVendorId() == (short) 0x16C0 &&
                        // info.getProductId() == (short) 0x05DF) {
                        //if (info.getVendorId() == (short) 0x16C0 && info.getProductId() == (short) 0x0a99) {
                        if (info.getVendorId() == (short) 0x2341 && info.getProductId() == (short) 0x8036) {
                            devInfo = info;
                            break;
                        }
                    }
                    if (devInfo == null) {
                        System.out.println("device not found");
                        Thread.sleep(1000);
                    } else {
                        System.out.println("device found");
                        if (true) {
                            deviceOpen = true;
                            final HidDevice dev = PureJavaHidApi.openDevice(devInfo);

                            //System.out.printf("onFeatureReport: id %d len %d data ", ret, 6);
                            //for (int i = 0; i < 6; i++)
                            //    System.out.printf("%02X ", data[i]);
                            //System.out.println();

                            dev.setDeviceRemovalListener(new DeviceRemovalListener() {
                                @Override
                                public void onDeviceRemoval(HidDevice source) {
                                    System.out.println("device removed");
                                    deviceOpen = false;

                                }
                            });
                            dev.setInputReportListener(new InputReportListener() {
                                @Override
                                public void onInputReport(HidDevice source, byte id, byte[] data, int len) {
                                    System.out.printf("onInputReport: id %d len %d data ", id, len);
                                    for (int i = 0; i < len; i++)
                                        System.out.printf("%02X ", data[i]);
                                    System.out.println();

                                    switch (id) {
                                        case 1:
                                            break;
                                        case DATA_REPORT_ID:
                                            DataReport report = new DataReport(id, data);
                                            break;
                                    }
                                }
                            });

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        byte[] data = new byte[7];
                                       /* byte[] data = new byte[132];
                                        data[0] = 1;
                                        int len = 0;
                                        if (((len = dev.getFeatureReport(data, data.length)) >= 0) && true) {
                                            int Id = data[0];
                                            System.out.printf("getFeatureReport: id %d len %d data ", Id, len);
                                            for (int i = 0; i < data.length; i++)
                                                System.out.printf("%02X ", data[i]);
                                            System.out.println();
                                        }*/
                                        switch (currentDataType) {
                                            case CMD_GET_VER:
                                                currentDataType = CMD_GET_STEER;
                                                break;
                                            case CMD_GET_STEER:
                                                currentDataType = CMD_GET_ANALOG;
                                                data[1] = currentDataIndex++;
                                                if (currentDataIndex > 6) {
                                                    currentDataIndex = 0;
                                                }
                                                break;
                                            case CMD_GET_ANALOG:
                                                currentDataType = CMD_GET_BUTTONS;
                                                break;
                                            case CMD_GET_BUTTONS:
                                                currentDataType = CMD_GET_GAINS;
                                                break;
                                            case CMD_GET_GAINS:
                                                currentDataType = CMD_GET_MISC;
                                                break;
                                            default:
                                                currentDataType = CMD_GET_VER;
                                                break;
                                        }
                                        data[0] = currentDataType;
                                        int ret = dev.setOutputReport(CMD_REPORT_ID, data, 7);
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }).start();


                            Thread.sleep(2000);
                            //dev.close();
                            //deviceOpen = false;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}