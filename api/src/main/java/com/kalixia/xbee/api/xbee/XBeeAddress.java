package com.kalixia.xbee.api.xbee;

import java.io.Serializable;

public interface XBeeAddress extends Serializable {
    XBeeAddress broadcast();

    /**
     * Return the length of the address in bytes.
     * @return the length of the address in bytes
     */
    int getLength();
}
