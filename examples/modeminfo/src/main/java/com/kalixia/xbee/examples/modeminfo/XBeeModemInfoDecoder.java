package com.kalixia.xbee.examples.modeminfo;

import com.kalixia.xbee.api.xbee.XBeeAtCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeModemInfoDecoder extends ChannelInboundMessageHandlerAdapter<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeModemInfoDecoder.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        ctx.write("ID\n");
//        ctx.write("exit\n");
        ctx.write(new XBeeAtCommand((byte) 1, "ID"));
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        LOGGER.info("Received {}", msg);
    }
}
