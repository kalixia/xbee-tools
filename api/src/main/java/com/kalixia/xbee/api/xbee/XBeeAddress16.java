package com.kalixia.xbee.api.xbee;

public class XBeeAddress16 implements XBeeAddress {
    private final short address;

    public XBeeAddress16(int address) {
        this((short) address);
    }

    public XBeeAddress16(short address) {
        this.address = address;
    }

    public short getValue() {
        return address;
    }

    public int getLength() {
        return 2;
    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XBeeAddress16 that = (XBeeAddress16) o;

        if (address != that.address) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) address;
    }
}
