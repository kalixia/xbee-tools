package com.kalixia.xbee.examples.hello;

import com.kalixia.xbee.api.xbee.XBeeAddress16;
import com.kalixia.xbee.api.xbee.XBeeTransmit;
import com.kalixia.xbee.api.xbee.XBeeTransmit16;
import com.kalixia.xbee.utils.XBeeFrameIdGenerator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class XBeeHelloEncoder extends MessageToMessageEncoder<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeHelloEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        if ("exit\n".equals(msg)) {
            ctx.close();
        }

//        LOGGER.info("Sending '{}'...", msg);
        out.add(new XBeeTransmit16(XBeeFrameIdGenerator.nextFrameID(), XBeeAddress16.BROADCAST,
                new XBeeTransmit.Options(false, false), msg.getBytes("UTF-8")));
    }
}
