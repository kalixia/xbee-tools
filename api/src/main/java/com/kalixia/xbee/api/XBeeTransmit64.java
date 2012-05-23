package com.kalixia.xbee.api;

public class XBeeTransmit64 implements XBeeTransmit {
    private final XBeeAddress64 destination;
    private final Options options;
    private final byte[] data;

    public XBeeTransmit64(XBeeAddress64 destination, Options options, byte[] data) {
        this.destination = destination;
        this.options = options;
        this.data = data;
    }

    public XBeeAddress64 getDestination() {
        return destination;
    }

    public Options getOptions() {
        return options;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "XBeeTransmit64{" +
                "destination=" + destination +
                ", options=" + options +
                '}';
    }
}
