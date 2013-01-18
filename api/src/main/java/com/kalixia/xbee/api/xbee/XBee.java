package com.kalixia.xbee.api.xbee;

public interface XBee {

    /**
     * Starts listening on the specified <tt>serialPort</tt> at the specified <tt>bauds</tt> rate.
     * @param serialPort the serial port used for communication with the XBee modem
     * @param bauds the bauds rate
     * @return the current XBee service
     * @throws InterruptedException if the serial port can't be opened properly
     */
    XBee begin(String serialPort, int bauds) throws InterruptedException;

    /**
     * Stops listening to the serial port and release underneath resources.
     * @return the current XBee service
     */
    XBee stop() throws InterruptedException;

    XBeeModemInfo getModemInfo() throws InterruptedException;

    XBeeModemStatus getModemStatus();

    XBee withModemConfiguration(XBeeModemConfiguration configuration);

    XBeeFuture send(XBeeRequest request);
    XBeeFuture sendAtRequest(String request);

    XBee addListener(XBeeListener listener);
}
