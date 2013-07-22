package com.kalixia.xbee.examples.echo;

import com.kalixia.xbee.api.xbee.XBeeAddress16;
import com.kalixia.xbee.api.xbee.XBeeReceive;
import com.kalixia.xbee.api.xbee.XBeeTransmit;
import com.kalixia.xbee.api.xbee.XBeeTransmit16;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeEchoHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeEchoHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        XBeeReceive request = (XBeeReceive) msg;
        LOGGER.info("Echoing {}", new String(request.getData()));
        XBeeTransmit16 response = new XBeeTransmit16((byte) 0, (XBeeAddress16) request.getSource(),
                new XBeeTransmit.Options(false, false), request.getData());
        ctx.write(response);
        ctx.flush();
    }

}
