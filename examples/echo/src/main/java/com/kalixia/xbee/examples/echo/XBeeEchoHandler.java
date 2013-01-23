package com.kalixia.xbee.examples.echo;

import com.kalixia.xbee.api.xbee.XBeeAddress16;
import com.kalixia.xbee.api.xbee.XBeeReceive;
import com.kalixia.xbee.api.xbee.XBeeTransmit;
import com.kalixia.xbee.api.xbee.XBeeTransmit16;
import com.kalixia.xbee.utils.XBeeFrameIdGenerator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeEchoHandler extends ChannelInboundMessageHandlerAdapter<XBeeReceive> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeEchoHandler.class);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, XBeeReceive request) throws Exception {
        LOGGER.info("Echoing {}", new String(request.getData()));
        XBeeTransmit16 response = new XBeeTransmit16(
                XBeeFrameIdGenerator.nextFrameID(),
                (XBeeAddress16) request.getSource(),
                new XBeeTransmit.Options(false, false),
                request.getData());
        ctx.write(response);
    }

}
