package com.kalixia.xbee.tools.recorder;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import com.kalixia.xbee.utils.HexUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ExceptionEvent;
import io.netty.channel.MessageEvent;
import io.netty.channel.SimpleChannelHandler;
import io.netty.channel.WriteCompletionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicLong;

public class XBeeRecorderHandler extends SimpleChannelHandler {
    private final Format format;
    private final FileOutputStream fos;
    private final ObjectOutputStream oos;
    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRecorderHandler.class);

    public XBeeRecorderHandler(String name, Format format) throws IOException {
        this.format = format;
        fos = new FileOutputStream(new File(name));
        oos = new ObjectOutputStream(fos);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof XBeeRequest) {
            XBeeRequest request = (XBeeRequest) e.getMessage();
            oos.writeObject(request);
            oos.flush();
            long count = counter.incrementAndGet();
            LOGGER.debug("Stored request {}: {}", count, request);
            switch (format) {
                case STRING:
                    System.out.printf("[XBee Packet %d] %s", count, new String(request.getData()));
                    break;
                case HEX:
                    System.out.printf("[XBee Packet %d] %s", count, HexUtils.toHexStringPrefixed(request.getData()));
                    break;
            }
        }
        super.messageReceived(ctx, e);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e) throws Exception {
//        if (e instanceof MessageEvent) {
//            XBeeRequest request = (XBeeRequest) ((MessageEvent) e).getMessage();
//            LOGGER.info("Storing: " + request);
//            LOGGER.info("should store response");
//        }
        super.writeComplete(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        LOGGER.error("Unexpected exception", e.getCause());
        Channel ch = e.getChannel();
        ch.close();
    }

}
