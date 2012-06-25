package com.kalixia.xbee.api.xbee;

/**
 * Configuration of the XBee module.
 *
 * Can either be used in order to set up the XBee modem or to read the actual configuration of the modem.
 */
public interface XBeeModemConfiguration {
    /**
     * Set the PAN ID. Casts the <tt>int</tt> to a <tt>short</tt> for ease of programming.
     *
     * @param panID the ID of the PAN
     * @return the configuration of the modem
     * @see #withPan(short)
     */
    XBeeModemConfiguration withPan(int panID);

    /**
     * Set the PAN ID.
     *
     * @param panID the ID of the PAN
     * @return the configuration of the modem
     * @see #withPan(int)
     */
    XBeeModemConfiguration withPan(short panID);

    /**
     * Set the channel. Casts the <tt>int</tt> to a <tt>byte</tt> for ease of programming.
     *
     * @param channel the channel to use
     * @return the configuration of the modem
     * @see #withChannel(byte)
     */
    XBeeModemConfiguration withChannel(int channel);

    /**
     * Set the channel
     *
     * @param channel the channel to use
     * @return the configuration of the modem
     * @see #withChannel(int)
     */
    XBeeModemConfiguration withChannel(byte channel);

    /**
     * Set the 16 bits address of the XBee module.
     *
     * @param address the 16 bits address
     * @return the configuration of the modem
     */
    XBeeModemConfiguration withAddress(XBeeAddress16 address);

    /**
     * Set the node identifier.
     *
     * @param nodeIdentifier the node identifier
     * @return the configuration of the modem
     */
    XBeeModemConfiguration withNodeIdentifier(String nodeIdentifier);

    /**
     * Set default destination address (either is 16 bits or 64 bits).
     *
     * @param destination the destination of the packets sent over XBee
     * @return the configuration of the modem
     */
    XBeeModemConfiguration withDestination(XBeeAddress destination);

    short getPanID();

    byte getChannel();

    XBeeAddress16 getAddress16();

    XBeeAddress64 getAddress64();

    String getNodeIdentifier();

    /**
     * Destination of the XBee packets.<br/>
     * Returned as set in DL/DH or as given with {@link #withDestination(XBeeAddress)}
     *
     * @return the destination of XBee packets
     */
    XBeeAddress getDestination();

    XBeeModemConfiguration build();
}
