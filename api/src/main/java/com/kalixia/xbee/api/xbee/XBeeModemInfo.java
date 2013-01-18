package com.kalixia.xbee.api.xbee;

import java.io.Serializable;

public class XBeeModemInfo implements Serializable {
    private short panID;
    private byte channel;
    private XBeeAddress16 localAddress;
    private String name;

    public short getPanID() {
        return panID;
    }

    public void setPanID(short panID) {
        this.panID = panID;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public XBeeAddress16 getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(XBeeAddress16 localAddress) {
        this.localAddress = localAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("XBeeModemInfo");
        sb.append("{panID=").append(panID);
        sb.append(", channel=").append(channel);
        sb.append(", localAddress=").append(localAddress);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
