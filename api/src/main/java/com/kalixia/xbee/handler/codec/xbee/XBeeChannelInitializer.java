package com.kalixia.xbee.handler.codec.xbee;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.MessageBuf;
import io.netty.channel.*;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.logging.ByteLoggingHandler;
import io.netty.handler.logging.MessageLoggingHandler;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

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
