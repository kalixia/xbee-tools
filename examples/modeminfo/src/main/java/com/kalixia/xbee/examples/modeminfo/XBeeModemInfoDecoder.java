package com.kalixia.xbee.examples.modeminfo;

import com.kalixia.xbee.api.xbee.XBeeAtCommand;
import com.kalixia.xbee.api.xbee.XBeeAtCommandResponse;
import com.kalixia.xbee.utils.XBeeFrameIdGenerator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XBeeModemInfoDecoder extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeModemInfoDecoder.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        /*
        ctx.write("ID");
        ctx.write("ND");

        // fake AT request command in order to exit from the example
        ctx.write("EX");
        */
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "ID"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "CH"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "MY"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "NI"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "DH"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "DL"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "AP"));
        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "CE"));
//        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "DB"));

        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "ND"));

        ctx.write(new XBeeAtCommand(XBeeFrameIdGenerator.nextFrameID(), "AI"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        XBeeAtCommandResponse response = (XBeeAtCommandResponse) msg;
        LOGGER.info("Received {}", response);
    }

}
