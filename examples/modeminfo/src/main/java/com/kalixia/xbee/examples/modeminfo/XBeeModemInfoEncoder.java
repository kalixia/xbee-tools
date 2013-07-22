package com.kalixia.xbee.examples.modeminfo;

import com.kalixia.xbee.api.xbee.XBeeAtCommand;
import com.kalixia.xbee.utils.XBeeFrameIdGenerator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Encode the given command as a {@link XBeeAtCommand}.
 */
public class XBeeModemInfoEncoder extends MessageToMessageEncoder<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeModemInfoEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        if ("EX".equals(msg)) {
            ctx.close();
        }

        out.add(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), msg));
    }
}
