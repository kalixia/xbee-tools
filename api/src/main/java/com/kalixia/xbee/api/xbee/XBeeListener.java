package com.kalixia.xbee.api.xbee;

public interface XBeeListener {
    XBee messageReceived(XBeeReceive message);
}
