package com.kalixia.xbee.examples.cosm.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CosmClientInboundHandler extends ChannelInboundMessageHandlerAdapter<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosmClientInboundHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object response) throws Exception {
        LOGGER.info("Received something: {}", response);
    }

}
