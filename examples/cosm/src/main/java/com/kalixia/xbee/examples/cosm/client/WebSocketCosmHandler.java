package com.kalixia.xbee.examples.cosm.client;

import com.kalixia.xbee.api.xbee.XBeeReceive;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WebSocketCosmHandler extends ChannelInboundMessageHandlerAdapter<XBeeReceive> {
    private final Channel cosmChannel;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketCosmHandler.class);

    public WebSocketCosmHandler(Channel cosmChannel) {
        this.cosmChannel = cosmChannel;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, XBeeReceive packet) throws Exception {
        LOGGER.info("Sending serial I/O event to COSM pipeline...");
        cosmChannel.write(new TextWebSocketFrame());
    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//        final Throwable t = e.getCause();
//        t.printStackTrace();
//        e.getChannel().close();
//    }

}
