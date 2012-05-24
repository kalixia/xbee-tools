package com.kalixia.xbee.api.xbee;

import java.io.Serializable;

/**
 * Received Signal Strength Indicator.
 * Hexadecimal equivalent of (-dBM) value.
 */
public class RSSI implements Serializable {
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
