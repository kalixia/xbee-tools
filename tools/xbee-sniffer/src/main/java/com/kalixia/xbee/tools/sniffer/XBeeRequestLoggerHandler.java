package com.kalixia.xbee.tools.sniffer;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import com.kalixia.xbee.utils.HexUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeRequestLoggerHandler extends ChannelInboundHandlerAdapter {
    private boolean hexDump;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestLoggerHandler.class);

    public XBeeRequestLoggerHandler() {}

    public XBeeRequestLoggerHandler(boolean hexDump) {
        this.hexDump = hexDump;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        XBeeRequest request = (XBeeRequest) msg;
        if (hexDump)
            LOGGER.info("Received XBee packet {}", HexUtils.toHexStringPrefixed(request.getData()));
        else
            LOGGER.info("Received XBee packet {}", new String(request.getData()));
    }
}
