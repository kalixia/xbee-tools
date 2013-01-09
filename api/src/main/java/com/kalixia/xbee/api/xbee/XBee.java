package com.kalixia.xbee.api.xbee;

public interface XBee {
    XBeeModemStatus getModemStatus();

    XBee withModemConfiguration(XBeeModemConfiguration configuration);

    XBeeFuture send(XBeeRequest request);
    XBeeFuture sendAtRequest(String request);

    XBee addListener(XBeeListener listener);
}
