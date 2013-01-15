package com.kalixia.xbee.examples.hello;

import com.kalixia.xbee.api.xbee.XBeeAddress16;
import com.kalixia.xbee.api.xbee.XBeeTransmit;
import com.kalixia.xbee.api.xbee.XBeeTransmit16;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeHelloEncoder extends MessageToMessageEncoder<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeHelloEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, String msg) throws Exception {
        if ("exit\n".equals(msg)) {
            ctx.close();
            return null;
        }

        return new XBeeTransmit16((byte) 1, XBeeAddress16.BROADCAST,
                new XBeeTransmit.Options(false, true), msg.getBytes("UTF-8"));
    }
}
