package com.kalixia.xbee.tools;

import com.kalixia.xbee.api.XBeeRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeRequestLoggerHandler extends SimpleChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestLoggerHandler.class);
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        XBeeRequest request = (XBeeRequest) e;
        LOGGER.info("Received XBee request {}", e);

        super.messageReceived(ctx, e);
    }
}
