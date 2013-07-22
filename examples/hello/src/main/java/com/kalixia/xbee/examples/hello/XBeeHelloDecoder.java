package com.kalixia.xbee.examples.hello;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeHelloDecoder extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeHelloDecoder.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.write("hello\n");
        ctx.write("world!\n");
        ctx.write("exit\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("Received {}", msg);
    }

}
