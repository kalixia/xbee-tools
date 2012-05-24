package com.kalixia.xbee.api.xbee;

public class XBeeReceive64 implements XBeeReceive {
    private final XBeeAddress64 source;
    private final RSSI rssi;
    private final Options options;
    private final byte[] data;

    public XBeeReceive64(XBeeAddress64 source, RSSI rssi, Options options, byte[] data) {
        this.source = source;
        this.rssi = rssi;
        this.options = options;
        this.data = data;
    }

    public XBeeAddress getSource() {
        return source;
    }

    public RSSI getRssi() {
        return rssi;
    }

    public Options getOptions() {
        return options;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "XBeeReceive64{" +
                "source=" + source +
                ", rssi=" + rssi +
                ", options=" + options +
                '}';
    }
}
