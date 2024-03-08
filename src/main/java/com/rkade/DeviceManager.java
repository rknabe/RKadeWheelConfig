package com.rkade;

import purejavahidapi.HidDeviceInfo;
import purejavahidapi.PureJavaHidApi;

public class DeviceManager {
    volatile static boolean deviceOpen = false;
    HidDeviceInfo devInfo = null;

    public DeviceManager() {

    }
}
