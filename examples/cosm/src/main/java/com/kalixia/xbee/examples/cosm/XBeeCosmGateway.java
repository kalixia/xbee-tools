package com.kalixia.xbee.examples.cosm;

import java.net.URISyntaxException;

public interface XBeeCosmGateway {
    void start(String serialPort, Integer baudRate, String apiKey, Long feedID, Integer datastreamID)
            throws InterruptedException, URISyntaxException;
}
