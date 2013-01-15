package com.kalixia.xbee.handler.codec.xbee;

enum XBeeApiIdentifier {
    TX_PACKET_64(0x00, "TX (Transmit) Request: 64-bit address"),
    TX_PACKET_16(0x01, "TX (Transmit) Request: 16-bit address"),
    AT_COMMAND(0x08, "AT Command"),
    RX_PACKET_64(0x80, "RX (Receive) Packet: 64-bit Address"),
    RX_PACKET_16(0x81, "RX (Receive) Packet: 16-bit Address"),
    AT_COMMAND_RESPONSE(0x88, "AT Command Response"),
    TX_STATUS(0x89, "TX (Transmit) Status"),
    MODEM_STATUS(0x8A, "Modem Status"),
    ZB_RX_PACKET(0x90, "ZigBee RX (Receive) Packet");

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
        throw new IllegalArgumentException(String.format("Unknown API identifier 0x%x", identifier));
    }

}
