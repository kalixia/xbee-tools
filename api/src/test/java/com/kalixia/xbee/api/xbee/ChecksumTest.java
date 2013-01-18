package com.kalixia.xbee.api.xbee;

import com.kalixia.xbee.handler.codec.xbee.XBeeRequestEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChecksumTest {

    @Test
    public void computeChecksum() {
        byte[] data = new byte[] { (byte) 0x01, (byte) 0x01, (byte) 0xFF, (byte) 0xFF,
                (byte) 0x00, (byte) 0x68, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x0A };

        ByteBuf buf = Unpooled.wrappedBuffer(data);

        assertEquals((byte) (0xe1 & 0xff), new XBeeRequestEncoder().calculateChecksum(buf));
    }
}
