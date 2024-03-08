package com.rkade;

import purejavahidapi.HidDevice;

public class Device {
    private String name;

    public Device(HidDevice hidDevice) {
        this.name = hidDevice.getHidDeviceInfo().getProductString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
