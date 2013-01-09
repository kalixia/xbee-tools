package com.kalixia.xbee.tools.recorder;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Encoder which transforms
 */
public class XBeeRequestEncoder extends MessageToMessageEncoder<XBeeRequest> {
    private final String name;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestEncoder.class);

    public XBeeRequestEncoder(String name) {
        this.name = name;
    }


    @Override
    protected Object encode(ChannelHandlerContext ctx, XBeeRequest request) throws Exception {
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(name, "w");
        } catch (FileNotFoundException fnfe) {
            LOGGER.error("Can't find file where to record", fnfe);
            return Unpooled.EMPTY_BUFFER;
        }
//            return new ChunkedFile(raf);
        FileChannel fileChannel = raf.getChannel();
//            ByteBuffer.allocate(10)
//            fileChannel.write()
        return fileChannel;
    }
}
