package com.kalixia.xbee.tools.recorder;

import com.kalixia.xbee.api.xbee.XBeeRequest;
import io.netty.buffer.ChannelBuffers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.oneone.OneToOneEncoder;
import io.netty.handler.stream.ChunkedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Encoder which transforms
 */
public class XBeeRequestEncoder extends OneToOneEncoder {
    private final String name;
    private static final Logger LOGGER = LoggerFactory.getLogger(XBeeRequestEncoder.class);

    public XBeeRequestEncoder(String name) {
        this.name = name;
    }


    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof XBeeRequest) {
            XBeeRequest request = (XBeeRequest) msg;
            RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(name, "w");
            } catch (FileNotFoundException fnfe) {
                LOGGER.error("Can't find file where to record", fnfe);
                return ChannelBuffers.EMPTY_BUFFER;
            }
//            return new ChunkedFile(raf);
            FileChannel fileChannel = raf.getChannel();
//            ByteBuffer.allocate(10)
//            fileChannel.write()
            return fileChannel;
        } else {
            return ChannelBuffers.EMPTY_BUFFER;
        }
    }
}
