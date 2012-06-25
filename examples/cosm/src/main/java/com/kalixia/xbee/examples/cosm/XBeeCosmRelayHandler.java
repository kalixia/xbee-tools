package com.kalixia.xbee.examples.cosm;

import com.kalixia.xbee.api.xbee.XBeeReceive;
import com.kalixia.xbee.examples.cosm.client.Cosm;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ExceptionEvent;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeCosmRelayHandler extends SimpleChannelUpstreamHandler {
    private final Cosm cosm;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosmRelayHandler.class);

    public XBeeCosmRelayHandler(Cosm cosm) {
        this.cosm = cosm;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        LOGGER.info("Sending serial I/O event to COSM pipeline...");
        XBeeReceive packet = (XBeeReceive) e.getMessage();
        cosm.getChannel().write(packet);
        super.messageReceived(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        final Throwable t = e.getCause();
        t.printStackTrace();
        e.getChannel().close();
    }
}
