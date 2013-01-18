package com.kalixia.xbee.handler.codec.xbee;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.handler.logging.ByteLoggingHandler;

public class XBeeChannelInitializer extends ChannelInitializer<RxtxChannel> {
    @Override
    public void initChannel(RxtxChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ByteLoggingHandler());

        pipeline.addLast("xbee-request-encoder", new XBeeRequestEncoder());
//        pipeline.addLast("xbee-request-decoder", new XBeeRequestDecoder());

        pipeline.addLast("xbee-frame-decoder", new XBeeFrameDelimiterDecoder());
        pipeline.addLast("xbee-packet-decoder", new XBeePacketDecoder(1));
    }
}
