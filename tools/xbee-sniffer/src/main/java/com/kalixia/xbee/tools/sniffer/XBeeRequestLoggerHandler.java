package com.kalixia.xbee.tools.sniffer;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import com.kalixia.xbee.utils.HexUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeRequestLoggerHandler extends ChannelInboundMessageHandlerAdapter<XBeeRequest> {
    private boolean hexDump;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestLoggerHandler.class);

    public XBeeRequestLoggerHandler() {}

    public XBeeRequestLoggerHandler(boolean hexDump) {
        this.hexDump = hexDump;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, XBeeRequest request) throws Exception {
        if (hexDump)
            LOGGER.info("Received XBee packet {}", HexUtils.toHexStringPrefixed(request.getData()));
        else
            LOGGER.info("Received XBee packet {}", new String(request.getData()));
    }
}
