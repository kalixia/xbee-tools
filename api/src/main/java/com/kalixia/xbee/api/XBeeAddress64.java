package com.kalixia.xbee.api;

public class XBeeAddress64 implements XBeeAddress {
    private final long address;

    public XBeeAddress64(long address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "0x" + Long.toHexString(address);
    }
}
