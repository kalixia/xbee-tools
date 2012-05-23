package com.kalixia.xbee.handler.codec.xbee;

enum XBeeApiIdentifier {
    MODEM_STATUS(0x8A, "Modem Status"),
    TX_PACKET_64(0x00, "TX (Transmit) Request: 64-bit address"),
    TX_PACKET_16(0x01, "TX (Transmit) Request: 16-bit address"),
    TX_STATUS(0x89, "TX (Transmit) Status"),
    RX_PACKET_64(0x80, "RX (Receive) Packet: 64-bit Address"),
    RX_PACKET_16(0x81, "RX (Receive) Packet: 16-bit Address");

    private byte identifier;
    private String description;

    XBeeApiIdentifier(int identifier, String description) {
        this.identifier = (byte) identifier;
        this.description = description;
    }

    public byte getApiIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public static XBeeApiIdentifier valueOf(byte identifier) {
        for (XBeeApiIdentifier id : values()) {
            if (id.getApiIdentifier() == identifier)
                return id;
        }
        throw new IllegalArgumentException("Unknown API identifier " + identifier);
    }

}
