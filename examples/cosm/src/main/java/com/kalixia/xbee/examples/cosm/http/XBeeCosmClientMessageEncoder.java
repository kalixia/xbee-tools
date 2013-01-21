package com.kalixia.xbee.examples.cosm.http;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * Encodes {@link com.kalixia.xbee.examples.cosm.XBeeData} objects into expected COSM {@link HttpRequest} in order to update a datastream.
 */
public class XBeeCosmClientMessageEncoder extends MessageToMessageEncoder<XBeeData> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeCosmClientMessageEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext ctx, XBeeData msg) throws Exception {
        String uri = String.format("/api/v2/feeds/%d/datastreams/%d/datapoints",
                msg.getFeedID(), msg.getDatastreamID());
//        String data = String.format("%s,%.2f", dateFormat.format(new Date()), msg.getValue());
        String data = String.format("%s,%.2f", "2013-01-21T20:15:00Z", msg.getValue());
        ByteBuf content = Unpooled.wrappedBuffer(data.getBytes());
        int contentLength = content.readableBytes();
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri);
        request.headers().set("X-ApiKey", msg.getApiKey());
        request.headers().set(HttpHeaders.Names.HOST, "api.cosm.com");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//        request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/csv");
//        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, contentLength);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, data.length());
        LOGGER.debug("About to send COSM request {}\n\t with content {}", request, data);
        return request;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Can't convert", cause);
    }
}
