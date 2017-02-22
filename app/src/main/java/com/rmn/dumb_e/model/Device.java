package com.rmn.dumb_e.model;

/**
 * Created by rmn on 09-09-2016.
 */
public class Device {
    String name;
    String mac;

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public String getName() {
        return name;
    }
}
