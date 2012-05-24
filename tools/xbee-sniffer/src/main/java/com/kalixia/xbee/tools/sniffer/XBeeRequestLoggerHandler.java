package com.kalixia.xbee.tools.sniffer;

import com.kalixia.xbee.api.XBeeReceive;
import com.kalixia.xbee.api.XBeeRequest;
import com.kalixia.xbee.utils.HexUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeRequestLoggerHandler extends SimpleChannelHandler {
    private boolean hexDump;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestLoggerHandler.class);

    public XBeeRequestLoggerHandler() {}

    public XBeeRequestLoggerHandler(boolean hexDump) {
        this.hexDump = hexDump;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        XBeeRequest receive = (XBeeRequest) e.getMessage();

        if (hexDump)
            LOGGER.info("Received XBee packet {}", HexUtils.toHexStringPrefixed(receive.getData()));
        else
            LOGGER.info("Received XBee packet {}", new String(receive.getData()));

        super.messageReceived(ctx, e);
    }
}
