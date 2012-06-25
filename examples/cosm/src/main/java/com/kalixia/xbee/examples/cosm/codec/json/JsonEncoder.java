package com.kalixia.xbee.examples.cosm.codec.json;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * Encode any Java object to a JSon representation using Jackson.
 */
public class JsonEncoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        //TODO: generate the appropriate JSon content based on the received packet
        String json =
                "{\n" +
                        "  \"datapoints\":[\n" +
                        "    {\"at\":\"2012-06-10T11:01:43Z\",\"value\":\"294\"}" +
                        "  ]\n" +
                        "}";
        return json;
    }
}
