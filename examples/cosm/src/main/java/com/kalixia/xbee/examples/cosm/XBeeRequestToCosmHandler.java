package com.kalixia.xbee.examples.cosm;

import com.kalixia.xbee.api.xbee.XBeeReceive;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeRequestToCosmHandler extends ChannelInboundHandlerAdapter {
    private final Channel cosmChannel;
    private final String apiKey;
    private final Long feedID;
    private final Integer datastreamID;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestToCosmHandler.class);

    public XBeeRequestToCosmHandler(Channel cosmChannel, String apiKey, Long feedID, Integer datastreamID) {
        this.cosmChannel = cosmChannel;
        this.apiKey = apiKey;
        this.feedID = feedID;
        this.datastreamID = datastreamID;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof XBeeReceive) {
            XBeeReceive received = (XBeeReceive) msg;
            LOGGER.debug("Received message from {} with RSSI {} and data {}",
                    new Object[] { received.getSource(), received.getRssi(), received.getData() });
            double value = Double.parseDouble(new String(received.getData()));
            cosmChannel.write(new XBeeData(apiKey, feedID, datastreamID, value));
        }  else {
            LOGGER.debug("Skipping message {}", msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("XBee reception", cause);
    }
}
