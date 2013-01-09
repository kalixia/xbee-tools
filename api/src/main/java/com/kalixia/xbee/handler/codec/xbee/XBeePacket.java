package com.kalixia.xbee.handler.codec.xbee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class XBeePacket {
    private final XBeeApiIdentifier apiIdentifier;
    private final int length;
    private final byte[] data;
    private final byte checksum;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeePacket.class);

    public XBeePacket(XBeeApiIdentifier apiIdentifier, int length, byte[] data) {
        this.apiIdentifier = apiIdentifier;
        this.length = length;
        this.data = data;
        this.checksum = calculateChecksum();
    }

    public XBeePacket(XBeeApiIdentifier apiIdentifier, int length, byte[] data, byte checksum) {
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

    public byte[] getData() {
        return data;
    }

    public byte getChecksum() {
        return checksum;
    }

    public byte calculateChecksum() {
        byte computed = apiIdentifier.getApiIdentifier();
        for (int i = 0; i < length - 1; i++) {
            computed += data[i];
        }
        return (byte) (0xFF - computed);
    }

    /**
     * Validate checksum of the data of the packet
     * @return true if the checksum is a match with the data
     */
    public boolean verifyChecksum() {
        byte computed = apiIdentifier.getApiIdentifier();
        for (int i = 0; i < length - 1; i++) {
            computed += data[i];
        }
        computed += checksum;
        LOGGER.debug("Computed checksum is {}", Integer.toHexString((byte)computed));
        return (computed == (byte) 0xFF);
    }
}
