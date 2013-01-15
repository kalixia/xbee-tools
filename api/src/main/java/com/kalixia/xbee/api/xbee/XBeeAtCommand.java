package com.kalixia.xbee.api.xbee;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class XBeeAtCommand implements XBeeRequest {
    private final byte frameID;
    private final String command;
    private final byte[] data;

    public XBeeAtCommand(byte frameID, String command) {
        this(frameID, command, null);
    }

    public XBeeAtCommand(byte frameID, String command, byte[] data) {
        if (command.length() != 2)
            throw new IllegalArgumentException("The expected command should have a length of two characters");

        this.frameID = frameID;
        this.command = command;
        this.data = data;
    }

    public byte getFrameID() {
        return frameID;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public ByteBuf serialize() {
        ByteBuf buf = Unpooled.buffer(1 + 2 + (data == null ? 0 : data.length));
        buf.writeByte(getFrameID());
        buf.writeBytes(command.getBytes());
        if (data != null)
            buf.writeBytes(data);
        return buf;
    }
}
