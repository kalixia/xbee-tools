package com.kalixia.xbee.api.xbee;

public class XBeeAddress64 implements XBeeAddress {
    private final long address;

    public XBeeAddress64(long address) {
        this.address = address;
    }

    public int getLength() {
        return 8;
    }

    @Override
    public String toString() {
        return "0x" + Long.toHexString(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XBeeAddress64 that = (XBeeAddress64) o;

        if (address != that.address) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (address ^ (address >>> 32));
    }
}
