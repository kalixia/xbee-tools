package com.kalixia.xbee.api;

public class XBeeTransmit16 implements XBeeTransmit {
    private final XBeeAddress16 destination;
    private final Options options;
    private final byte[] data;

    public XBeeTransmit16(XBeeAddress16 destination, Options options, byte[] data) {
        this.destination = destination;
        this.options = options;
        this.data = data;
    }

    public XBeeAddress16 getDestination() {
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
        return "XBeeTransmit16{" +
                "destination=" + destination +
                ", options=" + options +
                '}';
    }
}
