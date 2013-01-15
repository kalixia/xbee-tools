package com.kalixia.xbee.examples.hello;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeHelloEncoder extends MessageToByteEncoder<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeHelloEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        if ("exit\n".equals(msg)) {
            ctx.close();
            return;
        }

        byte apiIdentifier = 0x01;
        byte frameID = 0;
        byte dh = (byte) 0xFF;
        byte dl = (byte) 0xFF;
        byte options = 0;

        out.writeByte(0x7E);                            // start delimiter
        out.writeShort(1 + 1 + 2 + 1 + msg.length());   // length
        out.writeByte(apiIdentifier);                   // API identifier
        out.writeByte(frameID);                         // frame ID
        out.writeByte(dh);
        out.writeByte(dl);                              // destination address
        out.writeByte(options);                         // options

        for (int i = 0; i < msg.length(); i++)
            out.writeByte(msg.charAt(i));

        byte checksum = 0;
        checksum += apiIdentifier;
        checksum += frameID;
        checksum += dh;
        checksum += dl;
        checksum += options;

        for (int i = 0; i < msg.length(); i++)
            checksum += msg.charAt(i);

        out.writeByte(checksum);                        // checksum

        LOGGER.info("Sent {}", msg);
    }

}
