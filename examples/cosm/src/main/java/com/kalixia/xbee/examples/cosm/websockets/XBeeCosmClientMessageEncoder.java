package com.kalixia.xbee.examples.cosm.websockets;

import com.kalixia.xbee.examples.cosm.XBeeData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * Encodes {@link com.kalixia.xbee.examples.cosm.XBeeData} objects into expected COSM {@link io.netty.handler.codec.http.HttpRequest} in order to update a datastream.
 */
public class XBeeCosmClientMessageEncoder extends MessageToMessageEncoder<XBeeData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosmClientMessageEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, XBeeData msg) throws Exception {
        return new TextWebSocketFrame("{\n" +
                "  \"method\" : \"put\",\n" +
                "  \"resource\" : \"/feeds/" + msg.getFeedID() + "\",\n" +
                "  \"params\" : {},\n" +
                "  \"headers\" : {\"X-ApiKey\":\"" + msg.getApiKey() + "\"},\n" +
                "  \"body\" :\n" +
                "    {\n" +
                "      \"version\" : \"1.0.0\",\n" +
                "      \"datastreams\" : [\n" +
                "        {\n" +
                "          \"id\" : \"" + msg.getDatastreamID() + "\",\n" +
                "          \"current_value\" : \"" + msg.getValue() + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "  \"token\" : \"0x12345\"\n" +
                "}\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Can't convert", cause);
    }
}
