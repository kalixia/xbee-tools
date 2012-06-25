package com.kalixia.xbee.examples.cosm.client;

import io.netty.buffer.ChannelBuffer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channels;
import io.netty.channel.ExceptionEvent;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HttpSocketCosmHandler extends SimpleChannelHandler {
    private final String apiKey;
    private final Long feedID;
    private final Integer datastreamID;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpSocketCosmHandler.class);

    public HttpSocketCosmHandler(String apiKey, Long feedID, Integer datastreamID) {
        this.apiKey = apiKey;
        this.feedID = feedID;
        this.datastreamID = datastreamID;
    }

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof ChannelBuffer) {
            LOGGER.debug("Event is {}", e);
            HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                    "http://api.cosm.com/v2/feeds/" + feedID + "/datastreams/" + datastreamID + "/datapoints");
            request.setChunked(false);
            LOGGER.debug("Sending HTTP request to {}", request.getUri());
            request.setHeader(HttpHeaders.Names.HOST, "api.cosm.com");
            request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
    //            request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
            request.setHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.Names.ACCEPT, "application/json");
            request.setHeader(HttpHeaders.Names.USER_AGENT, "XBee Cosm Gateway");
            request.addHeader("X-ApiKey", apiKey);
            request.setContent((ChannelBuffer) e.getMessage());
            Channels.write(ctx.getChannel(), request);
        } else {
            super.writeRequested(ctx, e);
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        LOGGER.info("Received some data from COSM...");
        if (e.getMessage() instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) e.getMessage();
            LOGGER.info("Received HTTP status {}", response.getStatus());
//            LOGGER.debug("Data is:\n{}", response.getContent().toString(Charset.forName("UTF-8")));
        } else {
            super.messageReceived(ctx, e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        final Throwable t = e.getCause();
        t.printStackTrace();
        e.getChannel().close();
    }
}
