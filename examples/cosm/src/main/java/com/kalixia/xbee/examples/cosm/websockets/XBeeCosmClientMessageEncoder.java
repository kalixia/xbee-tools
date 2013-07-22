package com.kalixia.xbee.examples.cosm.websockets;

import com.kalixia.xbee.examples.cosm.XBeeData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Encodes {@link com.kalixia.xbee.examples.cosm.XBeeData} objects into expected COSM {@link io.netty.handler.codec.http.HttpRequest} in order to update a datastream.
 */
public class XBeeCosmClientMessageEncoder extends MessageToMessageEncoder<XBeeData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosmClientMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, XBeeData msg, List<Object> out) throws Exception {
        out.add(new TextWebSocketFrame("{\n" +
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
                "}\n"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Can't convert", cause);
    }
}
