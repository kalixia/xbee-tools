package com.kalixia.xbee.examples.modeminfo;

import com.kalixia.xbee.api.xbee.XBeeAtCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encode the given command as a {@link XBeeAtCommand}.
 */
public class XBeeModemInfoEncoder extends MessageToMessageEncoder<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeModemInfoEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, String msg) throws Exception {
        if ("exit\n".equals(msg)) {
            ctx.close();
            return null;
        }

        return new XBeeAtCommand((byte) 1, msg);
    }
}
