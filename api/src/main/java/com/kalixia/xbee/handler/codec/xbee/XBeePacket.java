package com.kalixia.xbee.handler.codec.xbee;

import io.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class XBeePacket {
    private final XBeeApiIdentifier apiIdentifier;
    private final int length;
    private final ChannelBuffer data;
    private final byte checksum;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeePacket.class);

    public XBeePacket(XBeeApiIdentifier apiIdentifier, int length, ChannelBuffer data) {
        this.apiIdentifier = apiIdentifier;
        this.length = length;
        this.data = data;
        this.checksum = calculateChecksum();
    }

    public XBeePacket(XBeeApiIdentifier apiIdentifier, int length, ChannelBuffer data, byte checksum) {
        this.apiIdentifier = apiIdentifier;
        this.length = length;
        this.data = data;
        this.checksum = checksum;
    }

    public XBeeApiIdentifier getApiIdentifier() {
        return apiIdentifier;
    }

    public int getLength() {
        return length;
    }

    public ChannelBuffer getData() {
        return data;
    }

    public byte getChecksum() {
        return checksum;
    }

    public byte calculateChecksum() {
        byte computed = apiIdentifier.getApiIdentifier();
        data.markReaderIndex();
        for (int i = 0; i < length - 1; i++) {
            computed += data.readByte();
        }
        data.resetReaderIndex();
        return (byte) (0xFF - computed);
    }

    /**
     * Validate checksum of the data of the packet
     * @return true if the checksum is a match with the data
     */
    public boolean verifyChecksum() {
        byte computed = apiIdentifier.getApiIdentifier();
        data.markReaderIndex();
        for (int i = 0; i < length - 1; i++) {
            computed += data.readByte();
        }
        computed += checksum;
        data.resetReaderIndex();
        LOGGER.debug("Computed checksum is {}", Integer.toHexString((byte)computed));
        return (computed == (byte) 0xFF);
    }
}
