package com.kalixia.xbee.api.xbee;

public enum XBeeModemStatus {
    HARDWARE_RESET(0), WATCHDOG_TIMER_RESET(1), ASSOCIATED(2), DISASSOCIATED(3),
    SYNCHRONIZATION_LOST(4), COORDINATOR_REALIGNMENT(5), COORDINATOR_STARTED(6);

    private byte value;

    XBeeModemStatus(int value) {
        this.value = (byte) value;
    }

    public int getValue() {
        return value;
    }

    public static XBeeModemStatus valueOf(byte value) {
        for (XBeeModemStatus status : values()) {
            if (status.getValue() == value)
                return status;
        }
        throw new IllegalArgumentException("Unknown XBee modem status " + value);
    }

    @Override
    public String toString() {
        return "XBeeModemStatus{" +
                "name=" + name() +
                ", value=" + value +
                '}';
    }
}
