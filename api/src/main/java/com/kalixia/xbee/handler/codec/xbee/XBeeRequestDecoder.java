package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeRequestDecoder extends MessageToMessageEncoder<XBeeRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestDecoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, XBeeRequest msg) throws Exception {
        return msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
