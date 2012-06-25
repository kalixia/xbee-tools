package com.kalixia.xbee.api.xbee;

public class XBeeModemConfigurationBuilder implements XBeeModemConfiguration {
    private short panID;
    private byte channel;
    private XBeeAddress16 address16;
    private XBeeAddress64 address64;
    private String nodeIdentifier;
    private XBeeAddress destination;

    @Override
    public XBeeModemConfiguration withPan(int panID) {
        return withPan((short) panID);
    }

    @Override
    public XBeeModemConfiguration withPan(short panID) {
        this.panID = panID;
        return this;
    }

    @Override
    public XBeeModemConfiguration withChannel(int channel) {
        return withChannel((byte) channel);
    }

    @Override
    public XBeeModemConfiguration withChannel(byte channel) {
        this.channel = channel;
        return this;
    }

    @Override
    public XBeeModemConfiguration withAddress(XBeeAddress16 address) {
        this.address16 = address;
        return this;
    }

    @Override
    public XBeeModemConfiguration withNodeIdentifier(String nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
        return this;
    }

    @Override
    public XBeeModemConfiguration withDestination(XBeeAddress destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public short getPanID() {
        return panID;
    }

    @Override
    public byte getChannel() {
        return channel;
    }

    @Override
    public XBeeAddress16 getAddress16() {
        return address16;
    }

    @Override
    public XBeeAddress64 getAddress64() {
        return address64;
    }

    @Override
    public String getNodeIdentifier() {
        return nodeIdentifier;
    }

    @Override
    public XBeeAddress getDestination() {
        return destination;
    }

    /**
     * Write the configuration to the XBee module.
     * @return the configuration of the modem
     */
    public XBeeModemConfiguration writeChanges() {
        // TODO: write this!
        return this;
    }

    /**
     * Read the configuration from the XBee module.
     * @return the configuration of the modem
     */
    public XBeeModemConfiguration getCurrentConfiguration() {
        // TODO: write this!
        return this;
    }

    public XBeeModemConfiguration build() {
        return this;
    }

}
