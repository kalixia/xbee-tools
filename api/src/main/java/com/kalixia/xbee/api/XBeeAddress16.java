package com.kalixia.xbee.api;

public class XBeeAddress16 implements XBeeAddress {
    private final int address;

    public XBeeAddress16(int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(address);
    }
}
