package com.kalixia.xbee.tools.recorder;

import io.netty.channel.ChannelOutboundMessageHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicLong;

public class XBeePlayerHandler extends ChannelOutboundMessageHandlerAdapter<Object> {
    private final FileInputStream fis;
    private final ObjectInputStream ois;
    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRecorderHandler.class);

    public XBeePlayerHandler(String name) throws IOException {
        fis = new FileInputStream(new File(name));
        ois = new ObjectInputStream(fis);
    }

//    public void writeRequested(ChannelHandlerContext ctx, Object obj) throws Exception {
//        super.writeRequested(ctx, obj);
//    }

}
