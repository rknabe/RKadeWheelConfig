package com.rkade;

import purejavahidapi.*;

import java.util.List;

public class App {
    volatile static boolean deviceOpen = false;
    private static byte currentDataType = DataReport.CMD_GET_VER;
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
                        deviceOpen = true;
                        if (deviceOpen) {
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
                                   //System.out.printf("onInputReport: id %d len %d data ", id, len);
                                    //for (int i = 0; i < len; i++)
                                     //   System.out.printf("%02X ", data[i]);
                                    //System.out.println();

                                    DataReport report = new DataReport(id, data);
                                    System.out.println(report);

                                    /*switch (id) {
                                        case 1:

                                            break;
                                        case DATA_REPORT_ID:
                                            DataReport report = new DataReport(id, data);
                                            break;
                                    }*/
                                }
                            });

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        byte[] data = new byte[7];
                                        data[0] = currentDataType;
                                        data[1] = currentDataIndex;
                                        int ret = dev.setOutputReport(DataReport.CMD_REPORT_ID, data, 7);
                                        if (ret > 0) {
                                            switch (currentDataType) {
                                                case DataReport.CMD_GET_VER:
                                                    currentDataIndex = 0;
                                                    currentDataType = DataReport.CMD_GET_STEER;
                                                    break;
                                                case DataReport.CMD_GET_STEER:
                                                    currentDataIndex = 0;
                                                    currentDataType = DataReport.CMD_GET_ANALOG;
                                                    break;
                                                case DataReport.CMD_GET_ANALOG:
                                                    ++currentDataIndex;
                                                    if (currentDataIndex > 6) {
                                                        currentDataIndex = 0;
                                                        currentDataType = DataReport.CMD_GET_BUTTONS;
                                                    }
                                                    break;
                                                case DataReport.CMD_GET_BUTTONS:
                                                    currentDataIndex = 0;
                                                    currentDataType = DataReport.CMD_GET_GAINS;
                                                    break;
                                                case DataReport.CMD_GET_GAINS:
                                                    currentDataIndex = 0;
                                                    currentDataType = DataReport.CMD_GET_MISC;
                                                    break;
                                                default:
                                                    currentDataIndex = 0;
                                                    currentDataType = DataReport.CMD_GET_VER;
                                                    break;
                                            }
                                        } else {
                                            System.out.println("Error sending OutputReport");
                                        }

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