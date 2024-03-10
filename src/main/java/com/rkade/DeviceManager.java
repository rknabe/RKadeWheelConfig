package com.rkade;

import com.fazecast.jSerialComm.SerialPort;
import purejavahidapi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rkade.DataReport.DATA_REPORT_ID;

public final class DeviceManager implements InputReportListener, DeviceRemovalListener {
    private final static int LEONARDO_VENDOR_ID = 0x2341;
    private final static int LEONARDO_PRODUCT_ID = 0x8036;
    private final static int OUTPUT_REPORT_DATA_LENGTH = 7;
    private final static byte AXIS_COUNT = 7;
    private final static List<DeviceListener> deviceListeners = new ArrayList<>();
    private static volatile boolean deviceAttached = false;
    private static volatile boolean versionReported = false;
    private static volatile HidDeviceInfo deviceInfo = null;
    private static volatile HidDevice openedDevice = null;

    public DeviceManager() {
        new Thread(new ConnectionRunner()).start();
        new Thread(new OutputReportRunner()).start();
    }

    public void addDeviceListener(DeviceListener deviceListener) {
        deviceListeners.add(deviceListener);
    }

    @Override
    public void onDeviceRemoval(HidDevice hidDevice) {
        System.out.println("device removed");
        deviceAttached = false;
        versionReported = false;
        deviceInfo = null;
        openedDevice = null;
        Device device = getDevice(hidDevice);
        notifyListenersDeviceDetached(device);
    }

    private void notifyListenersDeviceAttached(Device device) {
        for (DeviceListener deviceListener : deviceListeners) {
            deviceListener.deviceAttached(device);
        }
    }

    private void notifyListenersDeviceDetached(Device device) {
        for (DeviceListener deviceListener : deviceListeners) {
            deviceListener.deviceDetached(device);
        }
    }

    private void notifyListenersDeviceUpdated(Device device, String status, DataReport report) {
        for (DeviceListener deviceListener : deviceListeners) {
            deviceListener.deviceUpdated(device, status, report);
        }
    }

    private Device getDevice(HidDevice hidDevice) {
        return new Device(hidDevice);
    }

    @Override
    public void onInputReport(HidDevice hidDevice, byte id, byte[] data, int len) {
        if (id == DATA_REPORT_ID) {
            DataReport report = DataReportFactory.create(id, data);
            notifyListenersDeviceUpdated(new Device(hidDevice), null, report);
            if (report instanceof VersionDataReport) {
                versionReported = true;
            }

        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getOutputReport(byte dataType, byte dataIndex, byte[] data) throws IOException {
        data[0] = dataType;
        data[1] = dataIndex;
        int ret = openedDevice.setOutputReport(DataReport.CMD_REPORT_ID, data, OUTPUT_REPORT_DATA_LENGTH);
        if (ret <= 0) {
            throw new IOException("Device returned error for dataType:" + dataType + " dataIndex:" + dataIndex);
        }
    }

    private final class ConnectionRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (!deviceAttached) {
                    deviceInfo = null;
                    System.out.println("scanning");
                    notifyListenersDeviceUpdated(null, "Scanning...", null);
                    List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
                    for (HidDeviceInfo info : devList) {
                        if (info.getVendorId() == (short) LEONARDO_VENDOR_ID && info.getProductId() == (short) LEONARDO_PRODUCT_ID) {
                            deviceInfo = info;
                            break;
                        }
                    }
                    if (deviceInfo == null) {
                        System.out.println("device not found");
                        notifyListenersDeviceUpdated(null, "Device Not Found...", null);
                        sleep(1000);
                    } else {
                        System.out.println("device found");
                        notifyListenersDeviceUpdated(null, "Attached...", null);
                        deviceAttached = true;
                        if (deviceAttached) {
                            try {
                                openedDevice = PureJavaHidApi.openDevice(deviceInfo);
                                if (openedDevice != null) {
                                    Device device = getDevice(openedDevice);
                                    notifyListenersDeviceUpdated(device, "Opened", null);
                                    SerialPort[] ports = SerialPort.getCommPorts();
                                    for (SerialPort port : ports) {
                                        if (port.getVendorID() == LEONARDO_VENDOR_ID && port.getProductID() == LEONARDO_PRODUCT_ID) {
                                            device.setName(port.getDescriptivePortName());
                                        }
                                    }
                                    notifyListenersDeviceAttached(device);
                                    openedDevice.setDeviceRemovalListener(DeviceManager.this);
                                    openedDevice.setInputReportListener(DeviceManager.this);
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                sleep(2000);
            }
        }
    }

    private final class OutputReportRunner implements Runnable {

        @Override
        public void run() {
            int failCount = 0;
            byte[] data = new byte[OUTPUT_REPORT_DATA_LENGTH];
            while (true) {
                if (openedDevice != null) {
                    try {
                        if (!versionReported) {
                            //only need to do this once
                            System.out.println("getv");
                            getOutputReport(DataReport.CMD_GET_VER, (byte) 0, data);
                        }
                        getOutputReport(DataReport.CMD_GET_STEER, (byte) 0, data);
                        for (byte i = 0; i < AXIS_COUNT; i++) {
                            getOutputReport(DataReport.CMD_GET_ANALOG, i, data);
                        }
                        getOutputReport(DataReport.CMD_GET_BUTTONS, (byte) 0, data);
                        getOutputReport(DataReport.CMD_GET_GAINS, (byte) 0, data);
                        getOutputReport(DataReport.CMD_GET_MISC, (byte) 0, data);
                        failCount = 0;
                     } catch (IOException ex) {
                        System.out.println("Error sending OutputReport");
                        ++failCount;
                        if (failCount > 3) {
                            onDeviceRemoval(openedDevice);
                        }
                        sleep(1000);
                        ex.printStackTrace();
                    }
                } else {
                    sleep(1000);
                }
            }
        }
    }
}