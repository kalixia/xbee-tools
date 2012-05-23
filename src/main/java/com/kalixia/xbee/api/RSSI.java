package com.kalixia.xbee.api;

/**
 * Received Signal Strength Indicator.
 * Hexadecimal equivalent of (-dBM) value.
 */
public class RSSI {
    private final int value;

    public RSSI(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "RSSI{" +
                "value=" + Integer.toHexString(value) +
                ",dbm=" + value * -1 +
                '}';
    }
}
