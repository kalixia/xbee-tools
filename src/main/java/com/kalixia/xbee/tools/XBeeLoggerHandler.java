package com.kalixia.xbee.tools;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeLoggerHandler extends SimpleChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeLoggerHandler.class);
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        LOGGER.info("Received event {}", e);

        super.messageReceived(ctx, e);
    }
}
