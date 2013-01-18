package com.kalixia.xbee.api.xbee;

public class XBeeFactory {
    public static XBee newInstance() {
        return new XBeeImpl();
    }
}
