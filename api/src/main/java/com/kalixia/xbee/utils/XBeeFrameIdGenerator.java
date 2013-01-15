package com.kalixia.xbee.utils;

/**
 * Generator keeping track of last used frame ID in order to sequentially increment them.
 */
public class XBeeFrameIdGenerator {
    private static byte lastID = 0;

    /**
     * Returns the next frame ID.
     * @return
     */
    public synchronized static byte nextFrameID() {
        return ++lastID;
    }
}
