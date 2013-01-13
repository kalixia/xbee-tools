package com.kalixia.xbee.examples.hello;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class XBeeHelloDecoder extends ByteToMessageDecoder {
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in.discardReadBytes();
        return "";
    }
}
