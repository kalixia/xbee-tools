package com.kalixia.xbee.api.xbee;

public interface XBee {
    XBeeModemStatus getModemStatus();

    XBee withModemConfiguration(XBeeModemConfiguration configuration);

    XBeeReceive send(XBeeRequest request);

    XBee addListener(XBeeListener listener);
}
