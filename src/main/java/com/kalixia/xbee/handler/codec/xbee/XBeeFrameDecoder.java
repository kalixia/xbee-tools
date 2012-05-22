package com.kalixia.xbee.handler.codec.xbee;

import io.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

public class XBeeFrameDecoder extends LengthFieldBasedFrameDecoder {
    public XBeeFrameDecoder(int maxFrameLength,
                            int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                            int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }
}
