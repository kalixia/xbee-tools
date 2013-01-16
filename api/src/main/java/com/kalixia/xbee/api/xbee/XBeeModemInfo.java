package com.kalixia.xbee.api.xbee;

import java.io.Serializable;

// TODO: extract here usual XBee modem parameters.
public class XBeeModemInfo implements Serializable {
    private final short panID;

    public XBeeModemInfo(short panID) {
        this.panID = panID;
    }

    public short getPanID() {
        return panID;
    }
}
