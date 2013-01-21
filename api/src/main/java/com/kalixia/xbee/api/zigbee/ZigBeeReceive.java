package com.kalixia.xbee.api.zigbee;

import com.kalixia.xbee.api.xbee.RSSI;
import com.kalixia.xbee.api.xbee.XBeeAddress;
import com.kalixia.xbee.api.xbee.XBeeAddress16;
import com.kalixia.xbee.api.xbee.XBeeAddress64;
import com.kalixia.xbee.api.xbee.XBeeReceive;
import io.netty.buffer.ByteBuf;

public class ZigBeeReceive implements ZigBeeRequest, XBeeReceive {
    private final byte frameID;
    private final XBeeAddress64 source64;
    private final XBeeAddress16 source16;
    private final XBeeReceive.Options options;
    private final byte[] data;

    public ZigBeeReceive(byte frameID, XBeeAddress64 source64, XBeeAddress16 source16,
                         XBeeReceive.Options options, byte[] data) {
        this.frameID = frameID;
        this.source64 = source64;
        this.source16 = source16;
        this.options = options;
        this.data = data;
    }

    public byte getFrameID() {
        return frameID;
    }

    public XBeeAddress64 getSource64() {
        return source64;
    }

    public XBeeAddress16 getSource16() {
        return source16;
    }

    @Override
    public XBeeAddress getSource() {
        return source64;
    }

    @Override
    public RSSI getRssi() {
        return null;        // TODO: figure out how to get it!!!
    }

    public XBeeReceive.Options getOptions() {
        return options;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public ByteBuf serialize() {
        throw new UnsupportedOperationException();
    }
}
