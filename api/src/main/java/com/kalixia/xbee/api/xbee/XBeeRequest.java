package com.kalixia.xbee.api.xbee;

import java.io.Serializable;

public interface XBeeRequest extends Serializable {
    byte[] getData();
}
