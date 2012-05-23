package com.kalixia.xbee.api;

import java.util.BitSet;

public class XBeeTransmitStatus {
    private final byte frameID;
    private final byte status;

    public XBeeTransmitStatus(byte frameID, byte status) {
        this.frameID = frameID;
        this.status = status;
        BitSet bs = BitSet.valueOf(new byte[] { status });
    }

    /**
     * Only received if Frame ID was set in the TX request.
     * @return the frame ID
     */
    public byte getFrameID() {
        return frameID;
    }

    /**
     * The status of the transmission.
     * @return  <tt>1</tt> when all retries are expired and no ACK is received<br/>
     *          <tt>0</tt> or <tt>2</tt> if transmitter broadcasts<br/>
     *          <tt>3</tt> when the coordinator times out of an indirect transmission
     */
    public byte getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "XBeeTransmitStatus{" +
                "frameID=0x" + Integer.toHexString(frameID) +
                ", status=0x" + Integer.toHexString(status) +
                '}';
    }

}
