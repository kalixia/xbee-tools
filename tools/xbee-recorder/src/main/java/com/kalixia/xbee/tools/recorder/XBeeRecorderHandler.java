package com.kalixia.xbee.tools.recorder;

import com.kalixia.xbee.api.xbee.XBeeReceive;
import com.kalixia.xbee.utils.HexUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicLong;

public class XBeeRecorderHandler extends ChannelInboundMessageHandlerAdapter<XBeeReceive> {
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
    protected void messageReceived(ChannelHandlerContext ctx, XBeeReceive request) throws Exception {
        oos.writeObject(request);
        oos.flush();
        long count = counter.incrementAndGet();
        LOGGER.debug("Stored request {}: {}", count, request);
        switch (format) {
            case STRING:
                System.out.printf("[XBee Packet %3d] [RSSI: %s ] [Source: %s] %s%n", count,
                        request.getRssi(), request.getSource(),
                        new String(request.getData()));
                break;
            case HEX:
                System.out.printf("[XBee Packet %3d] [RSSI: %s ] [Source: %s] %s%n", count,
                        request.getRssi(), request.getSource(),
                        HexUtils.toHexStringPrefixed(request.getData()));
                break;
        }
    }

}
