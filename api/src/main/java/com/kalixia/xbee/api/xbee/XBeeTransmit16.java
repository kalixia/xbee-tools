package com.kalixia.xbee.api.xbee;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class XBeeTransmit16 implements XBeeTransmit {
    private final byte frameID;
    private final XBeeAddress16 destination;
    private final Options options;
    private final byte[] data;

    public XBeeTransmit16(byte frameID, XBeeAddress16 destination, Options options, byte[] data) {
        this.frameID = frameID;
        this.destination = destination;
        this.options = options;
        this.data = data;
    }

    public byte getFrameID() {
        return frameID;
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
    public ByteBuf serialize() {
        ByteBuf buf = Unpooled.buffer(1 + 2 + 1 + data.length);
        buf.writeByte(getFrameID());
        buf.writeShort(getDestination().getValue());
        buf.writeByte(getOptions().getValue());
        buf.writeBytes(data);
        return buf;
    }

    @Override
    public String toString() {
        return "XBeeTransmit16{" +
                "destination=" + destination +
                ", options=" + options +
                '}';
    }
}
