package com.kalixia.xbee.api.xbee;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public interface XBeeRequest extends Serializable {
    byte[] getData();

    /**
     * Serialize the request as a {@link ByteBuf} for serialization to the XBee Module.
     * @return the data to send to the XBee module
     */
    ByteBuf serialize();

}
